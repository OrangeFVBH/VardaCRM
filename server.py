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


def load_db():
    default_db = {"groups": [], "g_id": 1, "s_id": 1}
    if not os.path.exists(DATA_FILE): return default_db
    try:
        with open(DATA_FILE, "r", encoding="utf-8") as f:
            content = f.read()
            return json.loads(content) if content else default_db
    except Exception:
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
    # ИСПРАВЛЕНИЕ: Проверка на дубликаты групп
    if any(g["name"] == group.name for g in db["groups"]):
        raise HTTPException(status_code=400, detail="Группа уже существует")

    group.id = db["g_id"]
    db["g_id"] += 1
    db_group = group.dict()
    db["groups"].append(db_group)
    save_db()
    return db_group


@app.post("/students/{group_id}")
def add_student(group_id: int, student: StudentModel):
    for group in db["groups"]:
        if group["id"] == group_id:
            # Генерация нового ID для ученика
            student.id = db["s_id"]
            db["s_id"] += 1

            new_student = student.dict()
            group["students"].append(new_student)
            save_db()
            return new_student

    raise HTTPException(status_code=404, detail="Группа не найдена")

@app.delete("/groups/{group_id}")
def delete_group(group_id: int):
    # ИСПРАВЛЕНИЕ: Если группа не найдена — 404
    initial_len = len(db["groups"])
    db["groups"] = [g for g in db["groups"] if g["id"] != group_id]
    if len(db["groups"]) == initial_len:
        raise HTTPException(status_code=404, detail="Группа не найдена")
    save_db()
    return {"status": "success"}


# НОВЫЙ МЕТОД: Обновление данных ученика (включая дату)
@app.put("/students/{group_id}/{student_id}")
def update_student(group_id: int, student_id: int, updated_student: StudentModel):
    for group in db["groups"]:
        if group["id"] == group_id:
            for i, s in enumerate(group["students"]):
                if s["id"] == student_id:
                    updated_data = updated_student.dict()
                    updated_data["id"] = student_id  # Сохраняем оригинальный ID
                    group["students"][i] = updated_data
                    save_db()
                    return updated_data
    raise HTTPException(status_code=404, detail="Ученик не найден")


@app.delete("/students/{group_id}/{student_id}")
def delete_student(group_id: int, student_id: int):
    for group in db["groups"]:
        if group["id"] == group_id:
            group["students"] = [s for s in group["students"] if s["id"] != student_id]
            save_db()
            return {"status": "success"}
    raise HTTPException(status_code=404, detail="Группа не найдена")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
