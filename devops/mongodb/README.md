# MongoDB local setup

### 1. Setup MongoDB users
This will setup an `admin` with root privileges and a `linton` user with `readWriteAnyDatabase` rights.
From the `devops/mongodb` directory run
```shell
./mongo-local-setup.sh
```

### 2. Enable security for MongoDB
Edit the file `/usr/local/etc/mongod.conf` by adding the following lines:
```shell
security:
  authorization: enabled
```

### 3. Restart MongoDB
```shell
brew services restart mongodb/brew/mongodb-community
```

### 4. Test authentication
```shell
mongosh --port 27017 -u root -p Zepass123! --authenticationDatabase "admin"
```

You should now be connected to the local Mongo DB console.