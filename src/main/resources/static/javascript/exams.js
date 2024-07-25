document.addEventListener('DOMContentLoaded', function () {
    let createExamButton = document.querySelector('.create-exam-submit')
    let createExamBox = document.querySelector('.create-exam-box')
    let editExamBox = document.querySelector('.edit-exam-box')
    let cancelCreateButton = document.querySelector('.cancel-create-exam')
    let cancelEditButton = document.querySelector('.cancel-edit-exam')
    let overlay = document.getElementById('overlay')

    const addQuestionButton = document.querySelector('.addQuestion');
    const questionInputs = document.querySelector('.question-inputs');
    addQuestionButton.addEventListener('click' , function (){
        const newQuestion = document.createElement('div');
        newQuestion.classList.add('question');
        newQuestion.innerHTML = `
            <label for="questionContent">Tresc pytania:</label>
            <input type="text" id="questionContent" name="questionContent" />
            <label for="questionScore">Punkty za pytanie:</label>
            <input type="number" id="questionScore" name="questionScore" />
        `;
        questionInputs.appendChild(newQuestion);
    });

    //OD tu ja
        var dropdownButtons = document.querySelectorAll('.dropdown-btn');

        dropdownButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                var dropdownContent = this.nextElementSibling;
                dropdownContent.classList.toggle('show');
            });
        });

        window.onclick = function (event) {
            if (!event.target.matches('.dropdown-btn')) {
                var dropdowns = document.querySelectorAll('.dropdown-content');
                dropdowns.forEach(function (dropdown) {
                    if (dropdown.classList.contains('show')) {
                        dropdown.classList.remove('show');
                    }
                });
            }
        }
   //DO tu ja

    const createExam = document.querySelector('.createExam');
    createExam.addEventListener('click', function (){
        const examForm = document.getElementById('formularz');
        const formData = new FormData(examForm);

        const questionContents = document.querySelectorAll('input[name="questionContent"]');
        const questionScores = document.querySelectorAll('input[name="questionScore"]');
        const questions = [];

        questionContents.forEach((content, index) => {
            questions.push({
                content : content.value,
                score : questionScores[index].value
            });
        });
        const subjectid = document.getElementById('subjectidcreate').value;
        const description = document.getElementById('descriptioncreate').value;
        const startdate = document.getElementById('startdatecreate').value;
        const starttime = document.getElementById('starttimecreate').value;
        const enddate = document.getElementById('enddatecreate').value;
        const endtime = document.getElementById('endtimecreate').value;

        const csrf = document.querySelector('input[name="_csrf"]');

        const exam = {
            subject_id: subjectid,
            description: description,
            start_date: startdate,
            start_time: starttime,
            end_date: enddate,
            end_time: endtime,
            questions: questions
        }
        const jsonData = JSON.stringify(exam);
        console.log(jsonData);

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/addExamQuestions';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'examData';
        input.value = JSON.stringify(exam);

        const _csrf = document.createElement('input');
        _csrf.name = '_csrf';
        _csrf.type = 'hidden';
        _csrf.value = csrf.value

        form.appendChild(_csrf);
        form.appendChild(input);
        document.body.appendChild(form);

        form.submit();
    });


    createExamButton.addEventListener('click', function () {
        createExamBox.classList.toggle('hidden')
        overlay.classList.toggle('hidden')
    })


    cancelCreateButton.addEventListener('click', function () {
        createExamBox.classList.add('hidden')
        overlay.classList.add('hidden')
    })

    cancelEditButton.addEventListener('click', function () {
        editExamBox.classList.add('hidden')
        overlay.classList.add('hidden')
    })

})