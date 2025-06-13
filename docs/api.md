# API Documentation

## Authentication

### Login

```http
POST /index.php?action=login
Content-Type: application/x-www-form-urlencoded

username=user&password=pass
```

### Validate Token

```http
POST /index.php?action=validate-token
Content-Type: application/x-www-form-urlencoded

token=your_token
```

## Users

### Register

```http
POST /index.php?action=register
Content-Type: application/x-www-form-urlencoded

username=user&password=pass&id_rasp=raspberry_id
```

### Get Unassigned Raspberries

```http
GET /index.php?action=get-unassigned-raspberries
```

## Ports

### Get Ports

```http
POST /index.php?action=get-ports
Content-Type: application/x-www-form-urlencoded

username=user
```

### Update Ports

```http
POST /index.php?action=update-ports
Content-Type: application/x-www-form-urlencoded

username=user&porta1=value&porta2=value&...
```

## Raspberry

### Update URL

```http
POST /index.php?action=update-raspberry-url
Content-Type: application/x-www-form-urlencoded

id_rasp=raspberry_id&url=new_url
```

### Get All Raspberries

```http
GET /index.php?action=get-all-raspberries
```

## Response Format

All responses are in JSON format:

```json
{
  "success": true,
  "data": {
    // Response data
  }
}
```

Error responses:

```json
{
  "success": false,
  "error": "Error message"
}
```
