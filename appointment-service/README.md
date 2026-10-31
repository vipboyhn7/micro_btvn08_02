# Appointment-Service

Microservice này chạy ở cổng `8083` và gọi `Doctor-Service` mặc định ở `http://localhost:8082`.
Lỗi kết nối hoặc HTTP lỗi từ Doctor-Service được Circuit Breaker `doctorServiceCB` theo dõi.
Sau tối thiểu 5 lần gọi, nếu tỷ lệ lỗi đạt 50%, mạch chuyển từ `CLOSED` sang `OPEN`
và request tiếp theo nhận lỗi ngay thay vì chờ timeout.

## Chạy

```bash
mvn spring-boot:run
```

Có thể đổi địa chỉ Doctor-Service:

```bash
DOCTOR_SERVICE_URL=http://localhost:8082 mvn spring-boot:run
```

## Gọi thử

```bash
curl -i -X POST http://localhost:8083/api/appointments/check-availability \
  -H 'Content-Type: application/json' \
  -d '{"doctorId":"doctor-1","date":"2026-11-01","time":"09:00"}'
```

Tắt Doctor-Service rồi thực hiện 5 request để quan sát log:

```text
CircuitBreaker 'doctorServiceCB' changed state from CLOSED to OPEN
```

Trong trạng thái `OPEN`, response fallback có HTTP `503` và `"fallback": true`.
