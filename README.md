### use for install dependencies:
``` pip install -r requirements.txt ```

### add to .env:
`DATABASE_URI = mysql+pymysql://username:password@localhost/database
SECRET_KEY = secretkey`

### use for db:

```flask db init```
```flask db migrate -m "Initial migration."```
```flask db upgrade```

### use for run:

```python run.py```
