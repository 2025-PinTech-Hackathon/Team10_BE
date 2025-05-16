import requests
from bs4 import BeautifulSoup
import pymysql
import re
from datetime import datetime

# ✅ DB 연결 정보 설정
DB_HOST = '127.0.0.1'
DB_USER = 'root'      # 본인이 만든 사용자명
DB_PASSWORD = '1234'  # 사용자 비밀번호
DB_NAME = 'enp'

# ✅ 데이터베이스 및 테이블 초기화 함수
def init_db():
    conn = None
    try:
        conn = pymysql.connect(
            host=DB_HOST,
            port=3306,
            user=DB_USER,
            password=DB_PASSWORD,
            db=DB_NAME,
            charset='utf8mb4'
        )
        with conn.cursor() as cursor:
            cursor.execute("""
                CREATE TABLE IF NOT EXISTS newspaper (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title TEXT,
                    reporter VARCHAR(100),
                    content MEDIUMTEXT,
                    date DATETIME
                )
            """)
        conn.commit()
    except Exception as e:
        print("DB 초기화 중 에러:", e)
    finally:
        # conn이 아직 열려 있을 경우에만 닫기
        try:
            if conn and conn.open:
                conn.close()
        except:
            pass  # 이미 닫혔을 경우 에러 무시


# ✅ 1. 경제 섹션의 헤드라인 뉴스 URL 목록을 수집
def get_headline_urls():
    url = 'https://news.naver.com/section/101'
    headers = {'User-Agent': 'Mozilla/5.0'}
    res = requests.get(url, headers=headers)
    soup = BeautifulSoup(res.text, 'html.parser')

    urls = []
    articles = soup.select('a.sa_text_title')[:10]  # 셀렉터 수정
    for tag in articles:
        link = tag['href']
        if 'news.naver.com' in link:
            urls.append(link)
    return urls


# ✅ 2. 기사 상세 정보 추출
def get_article_details(url):
    headers = {'User-Agent': 'Mozilla/5.0'}
    res = requests.get(url, headers=headers)
    soup = BeautifulSoup(res.text, 'html.parser')

    title_tag = soup.select_one('h2#title_area')
    content_tag = soup.select_one('#dic_area')
    reporter_tag = soup.select_one('.byline_s')
    date_tag = soup.select_one('span.media_end_head_info_datestamp_time')

    title = title_tag.get_text(strip=True) if title_tag else ''
    content = re.sub(r'\s+', ' ', content_tag.get_text(strip=True)) if content_tag else ''
    reporter = reporter_tag.get_text(strip=True) if reporter_tag else '기자 미상'

    # ✅ 안정적인 날짜 파싱 방식: data-date-time 속성 활용
    if date_tag and date_tag.has_attr('data-date-time'):
        date_str = date_tag['data-date-time']  # 예: '2024-05-16 10:30:00'
        try:
            date = datetime.strptime(date_str, '%Y-%m-%d %H:%M:%S')
        except Exception as e:
            print(f"[⚠️ 날짜 파싱 실패] {e} / 원본: {date_str}")
            date = None
    else:
        print(f"[⚠️ 날짜 태그 없음] URL: {url}")
        date = None

    return {
        'title': title,
        'content': content,
        'reporter': reporter,
        'date': date,
        'url': url
    }


# ✅ 3. DB에 저장
def save_to_db(news_items):
    conn = pymysql.connect(
        host=DB_HOST,
        user=DB_USER,
        password=DB_PASSWORD,
        db=DB_NAME,
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor
    )
    with conn:
        with conn.cursor() as cursor:
            for item in news_items:
                sql = """
                INSERT INTO newspaper (title, reporter, content, date, summary)
                VALUES (%s, %s, %s, %s, %s)
                """
                cursor.execute(sql, (
                    item['title'],
                    item['reporter'],
                    item['content'],
                    item['date'],
                    None
                ))
        conn.commit()

# ✅ 전체 실행 함수
def run():
    init_db()  # DB 및 테이블 준비
    urls = get_headline_urls()
    news_data = [get_article_details(url) for url in urls]
    save_to_db(news_data)
    print(f"{len(news_data)}개의 뉴스가 저장되었습니다.")

# ✅ 진입점
if __name__ == '__main__':
    run()
