db = db.getSiblingDB('admin')
db.createUser({
        user: "linton",
        pwd: "Zepass123!",
        roles: [ "readWriteAnyDatabase" ]
    }
)
