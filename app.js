const express = require('express');
const mongoose = require('mongoose');
const registerRouter = require('./Routes/Register');

mongoose.connect('mongodb+srv://apoorv:apoorv21@findlost.7s1yz.mongodb.net/test?retryWrites=true&w=majority',{useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => console.log('Connected to MongoDB...'))
    .catch((err) => console.log(err));

let app = express();

app.use(express.json());
app.use('/me', (req, res) => {res.send('ONLINE !!!!')});
app.use('/register', registerRouter);

const PORT = process.env.PORT || 3000;

app.listen(PORT, ()=>{
    console.log('Listening to port ' + PORT);
});