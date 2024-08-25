# Как запустить?

## Предварительные требования

**Java 17** или выше: Убедитесь, что у вас установлена нужная версия Java. Проверить версию можно командой `java -version.`

**Git**: Необходим для клонирования репозитория, если проект не был скачан как архив.

**Docker:** Убедитесь, что у вас установлен Docker и он запущен.

## Запуск

Откройте `cmd`. Перейдите в любую папку с помощью команды `cd`. 

Пример: `cd C:\Users\User\Desktop\Task_managment`.

Потом пропишите `git clone https://github.com/MrDenyGrom/new_task_mang.git`.

Перейдите в проект `cd C:\Users\User\Desktop\Task_managment\new_task_mang`.

И наконец запустите docker compose

`docker-compose up --build`

В случае ошибки:  

```json
[backend 6/9] RUN ./mvnw dependency:go-offline

 > [backend 6/9] RUN ./mvnw dependency:go-offline:
: not foundw: line 20:
: not foundw: line 31:
0.266 ./mvnw: set: line 32: illegal option -

failed to solve: process "/bin/sh -c ./mvnw dependency:go-offline" did not complete successfully: exit code: 2
```

Скорее всего неправильно установились mvnw и mvnw.cmd. Тогда зайдите в архив и перекиньте в корневую папку проекта те файлы которые там есть и снова попробуйте `docker-compose up --build`.

Ждите.

После можно зайти на Swagger UI `http://localhost:8080/swagger-ui.html`




# Документация API для управления задачами

## 1. Введение

Данный RESTful API предоставляет функциональность для управления задачами. Он позволяет зарегистрированным пользователям создавать, редактировать, удалять задачи, управлять их статусом и оставлять комментарии. 

## 2. Базовый URL

http://localhost:8080

## 3. Аутентификация

API использует JWT авторизацию для контроля доступа к ресурсам. После успешной авторизации на endpoint `/user/login`  вы получите JWT токен. 

**Пример:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3MjQ1MTE5NzIsImV4cCI6MTcyNDU0Nzk3Mn0.cZ844X7knD5rNSGarT4Q77X5UzkKVRP7IVLy_bbTHIzju9KgJSsuWQSNkiPSvxJCCWhMzRGcEz1fwjsJCbqDZA"
}
```

## 3.1 Регистрация пользователя

**POST** `/user/register`

Регистрирует нового пользователя в системе.

**Тело запроса (application/json):**

```json
{
  "username": "123",
  "password": "123"
}
```

### Ответы:
**201 Created:** *Пользователь успешно зарегистрирован.*

**409 Conflict:** *Пользователь с таким именем уже существует.*

## 3.2 Авторизация пользователя
**POST** `/user/login`
Авторизует пользователя и возвращает JWT токен для доступа к защищенным endpoint'ам (всем остальным).

**Тело запроса (application/json):**

``` json
{
  "username": "123",
  "password": "123"
}
```

### Ответы:
**200 OK:** *Авторизация успешна.* 

Возвращается JWT токен в теле ответа:

``` json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJpYXQiOjE3MjQ1MTE5NzIsImV4cCI6MTcyNDU0Nzk3Mn0.cZ844X7knD5rNSGarT4Q77X5UzkKVRP7IVLy_bbTHIzju9KgJSsuWQSNkiPSvxJCCWhMzRGcEz1fwjsJCbqDZA"
}
```

## 4. Задачи

## 4.1 Создание задачи

**POST** `/tasks/create`

Создает новую задачу.

**Тело запроса (application/json):**

``` json
{
  "head": "string",
  "description": "string",
  "status": "WAITING",
  "priority": "HIGH",
  "executorUsername": "string"
}
```

**Возможные статусы задачи:**

*WAITING* - ожидает выполнения

*IN_PROGRESS* - в процессе выполнения

*COMPLETED* - выполнена

**Возможные приоритеты задачи:**

*HIGH* - высокий

*MEDIUM* - средний

*LOW* - низкий

### Ответы:
**200 OK:** *Задача успешно создана.* 

Возвращается объект созданной задачи:

``` json
{
  "id": 2,
  "head": "string",
  "description": "string",
  "status": "WAITING",
  "priority": "HIGH",
  "userAuthor": {
    "id": 1,
    "username": "string",
    "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
  },
  "userExecutor": {
    "id": 2,
    "username": "123",
    "password": "$2a$10$42ZKNO7fCIvyqsrbHLbXK.gpslRmEktBC6kCWU7FE474vbWUy8Tv2"
  }
}
```

## 4.2 Редактирование задачи

**PUT** `/tasks/edit/{id}`

Редактирует существующую задачу.

**Параметры:**

*id* (integer): ID задачи, которую нужно отредактировать.

Тело запроса (application/json):

``` json
{
  "head": "Новый заголовок",
  "description": "Новое описание",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "executorUsername": "новое_имя_пользователя_исполнителя"
}
```
### Ответы:
**200 OK:** *Задача успешно обновлена.*

**403 Forbidden:** *У вас нет прав на редактирование этой задачи (не вы создавали задачу).*

Возвращается объект обновленной задачи.

``` json
{
  "id": 2,
  "head": "string",
  "description": "string",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "userAuthor": {
    "id": 1,
    "username": "string",
    "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
  },
  "userExecutor": {
    "id": 2,
    "username": "123",
    "password": "$2a$10$42ZKNO7fCIvyqsrbHLbXK.gpslRmEktBC6kCWU7FE474vbWUy8Tv2"
  }
}
```

### 4.3 Удаление задачи

**DELETE** `/tasks/delete/{id}`

Удаляет задачу по ее ID.

**Параметры:**

*id* (integer): ID задачи, которую нужно удалить.

### Ответы:
**200 OK:** *Задача успешно удалена.*

**403 Forbidden:** *У вас нет прав на удаление этой задачи (не вы создавали задачу).*

В ответе успешность удаления:

``` json
Task successfully deleted
```

### 4.4 Получение задачи по ID

**GET** `/tasks/get/{id}`

Возвращает задачу по ее ID.

**Параметры:**

*id* (integer): ID задачи, которую нужно получить.

### Ответы:
**200 OK:** *Задача найдена.* 

Возвращается объект задачи:

```json
{
  "id": 3,
  "head": "string",
  "description": "string",
  "status": "WAITING",
  "priority": "HIGH",
  "userAuthor": {
    "id": 1,
    "username": "string",
    "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
  },
  "userExecutor": {
    "id": 2,
    "username": "123",
    "password": "$2a$10$42ZKNO7fCIvyqsrbHLbXK.gpslRmEktBC6kCWU7FE474vbWUy8Tv2"
  }
}
```

### 4.5 Получение списка задач пользователя

**GET** `/tasks/myTasks`

Возвращает список всех задач, созданных пользователем или назначенных на него.



### Ответы:
**200 OK:** *Успешно получено*

Возвращается список объектов задач залогиненного пользователя (и автор и исполнитель):

```json
[
  {
    "id": 1,
    "head": "string",
    "description": "string",
    "status": "WAITING",
    "priority": "HIGH",
    "userAuthor": {
      "id": 1,
      "username": "string",
      "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
    },
    "userExecutor": {
      "id": 1,
      "username": "string",
      "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
    }
  },
  {
    "id": 3,
    "head": "string",
    "description": "string",
    "status": "WAITING",
    "priority": "HIGH",
    "userAuthor": {
      "id": 1,
      "username": "string",
      "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
    },
    "userExecutor": {
      "id": 2,
      "username": "123",
      "password": "$2a$10$42ZKNO7fCIvyqsrbHLbXK.gpslRmEktBC6kCWU7FE474vbWUy8Tv2"
    }
  }
]
```

### 4.6 Обновление статуса задачи

**PUT** `/tasks/setStatus/{id}?status={status}`

Обновляет статус задачи.

**Параметры:**

*id* (integer): ID задачи, статус которой нужно обновить.

*status* (string): Новый статус задачи (WAITING, IN_PROGRESS, COMPLETED).

### Ответы:
**200 OK:** *Статус задачи успешно обновлен.* 

**403 Forbidden:** *У вас нет прав на изменение статуса этой задачи.*

Возвращается объект обновленной задачи.

```json
{
  "id": 3,
  "head": "string",
  "description": "string",
  "status": "COMPLETED",
  "priority": "HIGH",
  "userAuthor": {
    "id": 1,
    "username": "string",
    "password": "$2a$10$HGEUFRBP4S8KdJSdf5kvb.7clxWKTSkOhco3Sq5VdoklZMZId/jcm"
  },
  "userExecutor": {
    "id": 2,
    "username": "123",
    "password": "$2a$10$42ZKNO7fCIvyqsrbHLbXK.gpslRmEktBC6kCWU7FE474vbWUy8Tv2"
  }
}
```

### 5. Комментарии

### 5.1 Создание комментария

**POST** `/comments/{taskId}/create`

Создает новый комментарий к задаче.

**Параметры:**

*taskId* (integer): ID задачи, к которой нужно добавить комментарий.

Тело запроса (application/json):
``` json
{
  "text": "Текст комментария"
}
```

### Ответы:
**201 Created:** *Комментарий успешно создан*.

Возвращается объект созданного комментария c указанием имени:

```json
{
  "id": 1,
  "task": 3,
  "appUser": "string",
  "text": "newComment"
}
```

### 5.2 Получение комментариев к задаче

**GET** `/comments/{taskId}/comments`

Возвращает список всех комментариев к задаче.



**Параметры:**

*taskId* (integer): ID задачи, комментарии к которой нужно получить.

### Ответы:
**200 OK:** *
Возвращается список объектов комментариев:

```json
[
  {
    "id": 1,
    "task": 3,
    "appUser": "string",
    "text": "newComment"
  },
  {
    "id": 2,
    "task": 3,
    "appUser": "string",
    "text": "newnewComment"
  },
  {
    "id": 3,
    "task": 3,
    "appUser": "string",
    "text": "newnewnewComment"
  }
]
```











