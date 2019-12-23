db.createUser(
    {
        user: "appuser",
        pwd: "appuser",
        roles: [
            {
                role: "readWrite",
                db: "app"
            }
        ]
    }
);