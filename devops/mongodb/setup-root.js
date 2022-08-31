db = db.getSiblingDB('admin')
db.createUser({
        user: "admin",
        pwd: "Zepass123!",
        roles: [ { role: "root", db: "admin" } ]
    }
)
