import pymysql

# ✅ DB 연결 정보 설정
DB_HOST = '127.0.0.1'
DB_USER = 'root'      # 본인이 만든 사용자명
DB_PASSWORD = '1234'  # 사용자 비밀번호
DB_NAME = 'enp'

conn = pymysql.connect(
    host=DB_HOST,
    user=DB_USER,
    password=DB_PASSWORD,
    db=DB_NAME,
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

def reset_db():
    with conn:
        with conn.cursor() as cursor:
            sql = """
            DELETE FROM chat
            """
            cursor.execute(sql)
            conn.commit()
        with conn.cursor() as cursor:
            sql = """
            DELETE FROM newspaper
            """
            cursor.execute(sql)
            conn.commit()

def getUsers():
    with conn:
        with conn.cursor() as cursor:
            sql = """
            SELECT * FROM user
            """
            cursor.execute(sql)
            result = cursor.fetchall()
            return result

def resetCount(user):
    with conn:
        with conn.cursor() as cursor:
            sql = """
            UPDATE user
            SET todayQuizCount = 3
            WHERE id = %s
            """
            cursor.execute(sql, (user['id'],))
            conn.commit()

def close_db():
    conn.close()

# ✅ 전체 실행 함수
def run():
    reset_db()
    users = getUsers()
    for user in users:
        resetCount(user)
    close_db()
    print(f"DB 초기화 완료")

# ✅ 진입점
if __name__ == '__main__':
    run()
