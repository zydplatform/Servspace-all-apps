// src/models.js
const { DataTypes } = require('sequelize');
const { sequelize } = require('./config');

// User model
const User = sequelize.define('User', {
    username: {
        type: DataTypes.STRING,
        allowNull: false,
        unique: true,
    },
    password: {
        type: DataTypes.STRING,
        allowNull: false,
    },
});

// Task model
const Task = sequelize.define('Task', {
    taskId: {
        type: DataTypes.INTEGER,
        autoIncrement: true,
        primaryKey: true,
    },
    title: {
        type: DataTypes.STRING,
        allowNull: false,
    },
    taskDescription: {
        type: DataTypes.STRING,
        allowNull: true, // You can set this to false if you want it to be required
    },
    completed: {
        type: DataTypes.BOOLEAN,
        defaultValue: false,
    },
    userId: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: User,
            key: 'id',
        },
    },
});
// Define associations
User.hasMany(Task);
Task.belongsTo(User);

module.exports = { User, Task };
