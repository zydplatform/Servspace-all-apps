// src/app.js
const express = require('express');
const { connectDB, sequelize } = require('./config');
const routes = require('./routes');
const app = express();

// Connect to PostgreSQL
connectDB();

// Middleware
app.use(express.json());
app.use('/api', routes);

// Sync models with the database and start the server
const PORT = process.env.PORT || 5000;

sequelize.sync()
    .then(() => {
        app.listen(PORT, () => {
            console.log(`Server running on port ${PORT}`);
        });
    })
    .catch(err => {
        console.error('Unable to sync the database:', err);
    });
