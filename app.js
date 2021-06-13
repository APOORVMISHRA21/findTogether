const express = require('express');
const mongoose = require('mongoose');
const registerRouter = require('./Routes/Register');

mongoose.connect('mongodb://localhost:27017/findlost',{useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => console.log('Connected to MongoDB...'))
    .catch((err) => console.log(err));

let app = express();

app.use(express.json());
app.use('/Register', registerRouter);

const PORT = process.env.PORT || 3000;

app.listen(PORT, ()=>{
    console.log('Listening to port ' + PORT);
});