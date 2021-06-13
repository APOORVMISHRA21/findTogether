const mongoose = require('mongoose');
const express = require('express');
const brcypt = require('bcrypt');

const router = express.Router();
const { User, validateUser } = require('../Models/User.js');

router.use(express.json());

router.post('/', async (req, res) => {

    const { error } = validateUser(req.body);

    if(error) {
        return res.send(error.message);
    }

    const salt = await brcypt.genSalt(10);
    const pass = await brcypt.hash(req.body.password, salt);

    User.count({}, async (err, count) => {
        const user = User({
            userId : count + 1,
            email : req.body.email,
            password : pass
        });

        await user.save();

        res.send(user);
    });
});

module.exports = router;