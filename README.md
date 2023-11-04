
# Short Me

This is the Short Me application, a URL shortening service. You can use the following API routes to test the functionality:

## API Endpoints

### Shorten a URL
To shorten a URL, send a POST request to the `/api/shorten` endpoint with the long URL as plain text in the request body.

```bash
curl -X POST http://localhost:8080/api/shorten -H "Content-Type: text/plain" -d "https://www.example.com"
```

### Retrieve a Short URL
To retrieve the original URL from a short code, send a GET request to the `/api/retrieve` endpoint with the short URL as a query parameter.

```bash
curl -X GET "http://localhost:8080/api/retrieve?shortUrl=http://short.url/code"
```
