const mongoose = require('mongoose');
const Joi = require('joi');

const schema = Joi.object({
    firstName : Joi.string().required(),
    lastName : Joi.string().required(),
    email : Joi.string().required(),
    password : Joi.string().required()
});

function validateUser(user) {
    return schema.validate(user);
}

const userSchema = new mongoose.Schema({

    userId : {
        type : Number,
        required : true
    },

    firstName: {
        type : String,
        required : true,
        maxlength : 12
    },

    lastName: {
        type : String,
        required : true,
        maxlength : 12
    },

    email : {
        type : String,
        required : true,
        unique : true
    },

    password : {
        type : String,
        required : true,
        minlength : 5
    }
});


const User = mongoose.model('User', userSchema);

module.exports.User = User;
module.exports.validateUser = validateUser;