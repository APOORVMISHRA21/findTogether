const express = require('express');
const mongoose = require('mongoose');
const registerRouter = require('./Routes/register');
const loginRouter = require('./Routes/login');

// mongoose.connect(DB_URL,{useNewUrlParser: true, useUnifiedTopology: true})
//     .then(() => console.log('Connected to MongoDB...'))
//     .catch((err) => console.log(err));

mongoose.connect('mongodb+srv://apoorv:apoorv2101@findlost.7s1yz.mongodb.net/findlost?retryWrites=true&w=majority',{useNewUrlParser: true, useUnifiedTopology: true})
    .then(() => console.log('Connected to MongoDB...'))
    .catch((err) => console.log(err));

// mongoose.connect('mongodb://localhost:27017/findlost',{useNewUrlParser: true, useUnifiedTopology: true})
//     .then(() => console.log('Connected to MongoDB...'))
//     .catch((err) => console.log(err));


// if(!config.get('privateJwtKey')){
//     console.error('FATAL ERROR, ENV VAR NOT DEFINED');
//     process.exit(1);
// }
let app = express();

app.use(express.json());
app.use('/me', (req, res) => {res.send('ONLINE !!!!')});
app.use('/user', registerRouter);
app.use('/login', loginRouter);

const PORT = process.env.PORT || 3000;

app.listen(PORT, ()=>{
    console.log('Listening to port ' + PORT);
});