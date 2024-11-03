// src/routes.js
const express = require('express');
const { User, Task } = require('./models');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { authenticateToken } = require('./middleware');

const router = express.Router();

// Register User
router.post('/register', async (req, res) => {
    const { username, password } = req.body;
    const hashedPassword = await bcrypt.hash(password, 10);

    try {
        const user = await User.create({ username, password: hashedPassword });
        res.status(201).json({ message: 'User registered successfully' });
    } catch (err) {
        res.status(400).json({ message: 'User registration failed', error: err.message });
    }
});

// Login User
router.post('/login', async (req, res) => {
    const { username, password } = req.body;
    const user = await User.findOne({ where: { username } });

    if (!user || !(await bcrypt.compare(password, user.password))) {
        return res.status(401).json({ message: 'Invalid credentials' });
    }

    const accessToken = jwt.sign({ id: user.id }, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '5m' });
    res.json({ accessToken });
});

// Create a Task
router.post('/tasks', authenticateToken, async (req, res) => {
    const { title } = req.body;
    try {
        const task = await Task.create({ title, userId: req.user.id });
        res.status(201).json(task);
    } catch (err) {
        res.status(400).json({ message: 'Failed to create task', error: err.message });
    }
});

// Get All Tasks
router.get('/tasks', authenticateToken, async (req, res) => {
    try {
        const tasks = await Task.findAll({ where: { userId: req.user.id } });
        res.json(tasks);
    } catch (err) {
        res.status(500).json({ message: 'Failed to retrieve tasks', error: err.message });
    }
});

// Update a Task
router.put('/tasks/:id', authenticateToken, async (req, res) => {
    const { title, completed } = req.body;
    try {
        const task = await Task.findByPk(req.params.id);
        if (!task || task.userId !== req.user.id) return res.sendStatus(404); // Not Found
        
        task.title = title;
        task.completed = completed;
        await task.save();
        res.json(task);
    } catch (err) {
        res.status(400).json({ message: 'Failed to update task', error: err.message });
    }
});

// Delete a Task
router.delete('/tasks/:id', authenticateToken, async (req, res) => {
    try {
        const task = await Task.findByPk(req.params.id);
        if (!task || task.userId !== req.user.id) return res.sendStatus(404); // Not Found

        await task.destroy();
        res.json({ message: 'Task deleted' });
    } catch (err) {
        res.status(500).json({ message: 'Failed to delete task', error: err.message });
    }
});

module.exports = router;
