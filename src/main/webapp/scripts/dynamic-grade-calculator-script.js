//This script provides a dynamic form that shows/hides fields based on the user's selection

let form = document.getElementById("form");

//Question responses
let exam1Response = document.getElementById("exam1Response");
let exam2Response = document.getElementById("exam2Response");
let finalExamResponse = document.getElementById("finalExamResponse");
let labResponse = document.getElementById("labResponse");
let quizResponse = document.getElementById("quizResponse");

//Score labels
let exam1ScoreLabel = document.getElementById("exam1ScoreLabel");
let exam2ScoreLabel = document.getElementById("exam2ScoreLabel");
let finalExamScoreLabel = document.getElementById("finalExamScoreLabel");
let labScoreLabel = document.getElementById("labScoreLabel");
let quizScoreLabel = document.getElementById("quizScoreLabel");

//Response labels (show or hide depending on previous item response)
let exam2ResponseLabel = document.getElementById("exam2ResponseLabel");
let finalExamResponseLabel = document.getElementById("finalExamResponseLabel");

//Event listeners (show/hide fields based on input)
exam1Response.addEventListener("change", data => {
    if (data.target.value === "yes") {
        exam1ScoreLabel.hidden = false;
        exam2ResponseLabel.hidden = false;
    } else {
        //Hide all affected items
        exam1ScoreLabel.hidden = true;
        exam2ResponseLabel.hidden = true;
        exam2ScoreLabel.hidden = true;
        finalExamResponseLabel.hidden = true;
        finalExamScoreLabel.hidden = true;
    }
});

exam2Response.addEventListener("change", data => {
    if (data.target.value === "yes") {
        exam2ScoreLabel.hidden = false;
        finalExamResponseLabel.hidden = false;
    } else {
        exam2ScoreLabel.hidden = true;
        finalExamResponseLabel.hidden = true;
        finalExamScoreLabel.hidden = true;
    }
});

finalExamResponse.addEventListener("change", data => {
    if (data.target.value === "yes") {
        finalExamScoreLabel.hidden = false;
    } else {
        finalExamScoreLabel.hidden = true;
    }
});

labResponse.addEventListener("change", data => {
    if (data.target.value === "yes") {
        labScoreLabel.hidden = false;
    } else {
        labScoreLabel.hidden = true;
    }
});

quizResponse.addEventListener("change", data => {
    if (data.target.value === "yes") {
        quizScoreLabel.hidden = false;
    } else {
        quizScoreLabel.hidden = true;
    }
});

//Reset fields upon refresh
document.addEventListener("DOMContentLoaded", () => {
    form.reset();
});