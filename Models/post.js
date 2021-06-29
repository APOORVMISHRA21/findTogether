const mongoose = require('mongoose');
const Joi = require('joi');

const schema = Joi.object({
    creatorId : Joi.string().required(),
    creatorName : Joi.string().required(),
    category : Joi.string().required(),
    creationDate : Joi.string().required(),
    mediaUrl : Joi.string(),
    description : Joi.string()
});

function validatePost(post){
    return schema.validate(post);
}

const postSchema = new mongoose.Schema({
    creatorId : {
        type : String,
        required : true
    },

    creatorName : {
        type : String,
        required : true
    },

    category : {
        type : String,
        required : true
    },

    creationDate : {
        type : String,
        required : true
    },

    mediaUrl : {
        type : String,
    },

    description : {
        type : String,
    }
}); 

const Post = mongoose.model('Post', postSchema);

module.exports.Post = Post;
module.exports.validatePost = validatePost;