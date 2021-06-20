const mongoose = require('mongoose');
const express = require('express');
const Joi = require('joi');
const jwt = require('jsonwebtoken');
const brcypt = require('bcrypt');

const router = express.Router();
const { User, validateUser } = require('../Models/user.js');

router.use(express.json());

router.get('/', async (req, res) => {
    const user = await User.find();
    res.send(user);
});

router.post('/', async (req, res) => {

    const { error } = validateCredentials(req.body);

    if(error) return res.status(400).send(error.message);

    const user = await User.findOne({email:req.body.email});

    if(!user) return res.status(400).send("Invalid Email or Password");

    const is_pass = await brcypt.compare(req.body.password, user.password);

    if(!is_pass) return res.status(400).send("Invalid Email or Password");

    //res.send(true);
    
    // const token = jwt.sign({_id: user._id}, 'privateKey');

    // return res.send(token);

    return res.send({
        "firstName" : user.firstName,
        "lastName" : user.lastName,
        "email" : user.email
    });
});

const schema = Joi.object({
    email : Joi.string().required(),
    password : Joi.string().required()
});

function validateCredentials(user) {
    return schema.validate(user);
}


module.exports = router;