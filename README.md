# Тестовые пользователи

| Логин      | Пароль       | Роль               |
|------------|--------------|--------------------|
| admin      | admin123     | RoleEnum.ADMIN     |
| hostess    | hostess123   | RoleEnum.HOSTESS   |
| reception  | reception123 | RoleEnum.HOSTESS   |
| manager    | manager123   | RoleEnum.ADMIN     |
| staff      | staff123     | RoleEnum.WORKER    |

---

# Решение проблем с обработкой аннотаций

Если произойдет ошибка с постпроцессингом аннотаций

1. Откройте настройки IntelliJ IDEA:
   - **Windows/Linux**: `File -> Settings`
   - **macOS**: `IntelliJ IDEA -> Preferences`

2. Перейдите по пути: 
File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors

3. Для модуля `Annotation profile for hotelReservationService`:
- ✅ Отметьте "Enable annotation processing"
- 🔘 Выберите "Obtain processors from project classpath"

4. Пересоберите проект:
- `Build → Rebuild Project`
