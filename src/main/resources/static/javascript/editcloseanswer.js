function deleteAnswer(answerId, questionId) {
    console.log("deleteAnswer called with", answerId, questionId);
    var form = document.createElement('form');
    document.body.appendChild(form);
    form.method = 'post';
    form.action = '/deleteAnswer';

    var answerIdField = document.createElement('input');
    answerIdField.type = 'hidden';
    answerIdField.name = 'answerId';
    answerIdField.value = answerId;
    form.appendChild(answerIdField);

    var questionIdField = document.createElement('input');
    questionIdField.type = 'hidden';
    questionIdField.name = 'questionId';
    questionIdField.value = questionId;
    form.appendChild(questionIdField);

    var csrfToken = document.querySelector('input[name="_csrf"]').value;
    var csrfField = document.createElement('input');
    csrfField.type = 'hidden';
    csrfField.name = '_csrf';
    csrfField.value = csrfToken;
    form.appendChild(csrfField);

    form.submit();
}

function addNewAnswer(questionId) {
    console.log("addNewAnswer called with", questionId);
    var form = document.createElement('form');
    document.body.appendChild(form);
    form.method = 'post';
    form.action = '/addNewAnswer'; // Update the action URL as per your routing

    var questionIdField = document.createElement('input');
    questionIdField.type = 'hidden';
    questionIdField.name = 'questionId';
    questionIdField.value = questionId;
    form.appendChild(questionIdField);

    var csrfToken = document.querySelector('input[name="_csrf"]').value;
    var csrfField = document.createElement('input');
    csrfField.type = 'hidden';
    csrfField.name = '_csrf';
    csrfField.value = csrfToken;
    form.appendChild(csrfField);

    form.submit();
}
