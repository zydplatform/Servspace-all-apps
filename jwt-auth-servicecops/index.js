require('dotenv').config();
const express = require('express');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

const users = []; // Simulating a simple user database
let refreshTokens = []; // Store refresh tokens

// Token expiration times
const ACCESS_TOKEN_EXPIRATION = '5m'; // 5 minutes
const REFRESH_TOKEN_EXPIRATION = '3d'; // 3 days
const TOKEN_LEEWAY = 5 * 60; // 5 minutes in seconds

// Register a user
app.post('/register', (req, res) => {
  const { username, password } = req.body;
  const user = { username, password };
  users.push(user);
  res.json({ message: 'User registered successfully' });
});

// Generate Access and Refresh Tokens
app.post('/login', (req, res) => {
  const { username, password } = req.body;
  const user = users.find(u => u.username === username && u.password === password);
  
  if (!user) return res.status(401).json({ message: 'Invalid credentials' });

  // Generate tokens without including `exp` in the payload
  const accessToken = generateAccessToken({ username }); // Only include username
  const refreshToken = generateRefreshToken(user); // Generate refresh token
  refreshTokens.push(refreshToken);

  res.json({ accessToken, refreshToken });
});

// Token refresh endpoint
app.post('/token', (req, res) => {
  const { token } = req.body;
  
  if (!token || !refreshTokens.includes(token)) return res.status(403).json({ message: 'Forbidden' });

  jwt.verify(token, process.env.REFRESH_TOKEN_SECRET, (err, user) => {
    if (err) return res.status(403).json({ message: 'Forbidden' });

    // Generate new access token
    const accessToken = generateAccessToken({ username: user.username });
    res.json({ accessToken });
  });
});

// Secure route that requires a valid access token
app.get('/protected', authenticateToken, (req, res) => {
  res.json({ message: `Hello ${req.user.username}, you have accessed a protected endpoint!` });
});

// Middleware for authenticating the access token
function authenticateToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];
  
  if (!token) return res.status(401).json({ message: 'Unauthorized' });

  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, { clockTolerance: TOKEN_LEEWAY }, (err, user) => {
    if (err) return res.status(403).json({ message: 'Forbidden' });

    req.user = user;
    next();
  });
}

// Helper function to generate an access token
function generateAccessToken(user) {
  return jwt.sign(user, process.env.ACCESS_TOKEN_SECRET, { expiresIn: ACCESS_TOKEN_EXPIRATION });
}

// Helper function to generate a refresh token
function generateRefreshToken(user) {
  return jwt.sign(user, process.env.REFRESH_TOKEN_SECRET, { expiresIn: REFRESH_TOKEN_EXPIRATION });
}

// Logout: Invalidate the refresh token
app.delete('/logout', (req, res) => {
  const { token } = req.body;
  refreshTokens = refreshTokens.filter(t => t !== token);
  res.json({ message: 'Logout successful' });
});

app.listen(process.env.PORT, () => {
  console.log(`Server running on port ${process.env.PORT}`);
});
