from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import json
import os
import uvicorn

app = FastAPI()
DATA_FILE = "data.json"

class StudentModel(BaseModel):
    id: int
    name: str
    phoneNumber: str
    lastPaymentDate: str = ""
    subEndDate: str = "2025-01-01"
    photoUrl: Optional[str] = None

class GroupModel(BaseModel):
    id: int
    name: str
    coach: str
    schedule: str
    students: List[StudentModel] = []

# Безопасная загрузка базы данных
def load_db():
    default_db = {"groups": [], "g_id": 1, "s_id": 1}
    if not os.path.exists(DATA_FILE):
        return default_db
    try:
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            content = f.read()
            if not content: return default_db # Если файл пустой
            return json.loads(content)
    except Exception as e:
        print(f"Ошибка чтения файла: {e}")
        return default_db

db = load_db()

def save_db():
    with open(DATA_FILE, "w", encoding="utf-8") as f:
        json.dump(db, f, ensure_ascii=False, indent=4)

@app.get("/groups")
def get_groups():
    return db["groups"]

@app.post("/groups")
def add_group(group: GroupModel):
    group.id = db["g_id"]
    db["g_id"] += 1
    db_group = group.dict()
    db["groups"].append(db_group)
    save_db()
    return db_group

@app.delete("/groups/{group_id}")
def delete_group(group_id: int):
    global db
    db["groups"] = [g for g in db["groups"] if g["id"] != group_id]
    save_db()
    return {"status": "success"}

@app.post("/students/{group_id}")
def add_student(group_id: int, student: StudentModel):
    for group in db["groups"]:
        if group["id"] == group_id:
            student.id = db["s_id"]
            db["s_id"] += 1
            group["students"].append(student.dict())
            save_db()
            return student
    raise HTTPException(status_code=404, detail="Group not found")

@app.delete("/students/{group_id}/{student_id}")
def delete_student(group_id: int, student_id: int):
    for group in db["groups"]:
        if group["id"] == group_id:
            group["students"] = [s for s in group["students"] if s["id"] != student_id]
            save_db()
            return {"status": "success"}
    raise HTTPException(status_code=404, detail="Group not found")

if __name__ == "__main__":
    # Запуск прямо из скрипта, чтобы видеть ошибки в консоли
    uvicorn.run(app, host="0.0.0.0", port=8000)