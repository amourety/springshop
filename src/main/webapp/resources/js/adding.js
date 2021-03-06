function openFeedback() {
    id = "formFeedback";
    display = document.getElementById(id).style.display;
    if (display === 'none') {
        document.getElementById(id).style.display = 'block';
    } else {
        document.getElementById(id).style.display = 'none';
    }
}

function getImg() {
    $.ajax({
        type: 'GET',
        url: '/img'
    }).done(function () {
        location.reload();
    });
}


function doOrder() {
    if ($("#amountOfCart").text() === "Your cart is empty, please buy something") {
    } else {
        $("#amountOfCart").html("");
        $.ajax({
            type: 'POST',
            url: '/main',
            data: {
                action: 'addOrder',
            }
        }).done(function (data) {
                $("#sum").html("");
                getData();
            }
        ).fail(function (jqXHR, exception) {
            var msg = '';
            if (jqXHR.status === 0) {
                msg = 'Not connect.\n Verify Network.';
            } else if (jqXHR.status == 404) {
                msg = 'Requested page not found. [404]';
            } else if (jqXHR.status == 500) {
                msg = 'Internal Server Error [500].';
            } else if (exception === 'parsererror') {
                msg = 'Requested JSON parse failed.';
            } else if (exception === 'timeout') {
                msg = 'Time out error.';
            } else if (exception === 'abort') {
                msg = 'Ajax request aborted.';
            } else {
                msg = 'Uncaught Error.\n' + jqXHR.responseText;
            }
            alert(msg)
        })
        return false;
    }
}

function isEmpty(str) {
    if (str.trim() == '')
        return true;

    return false;
}


function sendingFeedback(productId) {
    let text = document.getElementById('exampleFormControlTextarea1').value;
    document.getElementById('exampleFormControlTextarea1').value = "";
    let rate = $("#example-bootstrap").val();
    $.ajax({
        type: 'POST',
        url: "/feedback",
        data: {
            productId: productId,
            text: text,
            rate: rate
        },
        data_type: 'json'
    }).done(function (data) {
        renderRating(productId);
        let tableHtml = "";
        tableHtml += '<ul class="list-group">';
        for (let i = 0; i < data.length; i++) {
            tableHtml += '<li class="list-group-item">';
            tableHtml += '<div class="alert alert-light" role="alert"><h5>' + data[i].username + '</h5> <h6>(' + (data[i].stringTime) + ')</h6>';
            tableHtml += '<h5 id="special">' + data[i].text + '</h5>';
            tableHtml += '<a style="font-size: 12px; color: #adabab;"> Rating:' + data[i].rate + '</a>';
            tableHtml += '</div>';
            tableHtml += '</li>';
        }
        tableHtml += '</ul>';

        $("#feedbacks").html(tableHtml);


    }).fail(function (jqXHR, exception) {
        var msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });
}

function renderRating(productId) {
    $.ajax({
        type: 'POST',
        url: "/rating",
        data: {
            productId: productId,
        },
        data_type: 'json'
    }).done(function (data) {
        $("#productRating").html(data.rating);
        $("#countFeedback").html(data.countReviews);
            }).fail(function (jqXHR, exception) {
        var msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });
}

function sendingMessage() {
    var email, name, surname, text;
    email = document.getElementById('exampleFormControlInput1').value;
    name = document.getElementById('exampleFormControlInput2').value;
    surname = document.getElementById('exampleFormControlInput3').value;
    text = document.getElementById('exampleFormControlTextarea1').value;
    if (isEmpty(text) || isEmpty(name) || isEmpty(surname) || isEmpty(email)) {
        $("#messageContact").html("<div class=\"alert alert-danger\"> <strong>Wrong!</strong> Fill a form! </div>");
    } else {
        $.ajax({
            type: 'POST',
            url: '/contacts',
            data: {
                action: 'sending',
                email: email,
                name: name,
                surname: surname,
                text: text
            }
        }).done(function () {
            renderAnswers();
            $("#messageContact").html("<div class=\"alert alert-success\"> <strong>Success!</strong> Your letter is sent! </div>");
        }).fail(function (jqXHR, exception) {
            var msg = '';
            if (jqXHR.status === 0) {
                msg = 'Not connect.\n Verify Network.';
            } else if (jqXHR.status == 404) {
                msg = 'Requested page not found. [404]';
            } else if (jqXHR.status == 500) {
                msg = 'Internal Server Error [500].';
            } else if (exception === 'parsererror') {
                msg = 'Requested JSON parse failed.';
            } else if (exception === 'timeout') {
                msg = 'Time out error.';
            } else if (exception === 'abort') {
                msg = 'Ajax request aborted.';
            } else {
                msg = 'Uncaught Error.\n' + jqXHR.responseText;
            }
            alert(msg)
        });
    }
    return false;
}

function doBuying(id, price) {
    let a = +$("#amountOfCart").text();
    a = a + 1;
    if (+a > 0) {
        $("#amountOfCart").html(a);
    }
    else {
        $("#amountOfCart").html("");
    }
    $.ajax({
        type: 'POST',
        url: '/main',
        data: {
            action: 'buy',
            product_id: id,
            price: price
        }
    }).done(function (data) {
        let tableHtml = "";
        let sum = 0;
        let amount = 0;
        if (data.length === 0) {
            tableHtml += '<a class="dropdown-item" href="#">Your cart is empty</a>';
        } else {
            for (let i = 0; i < data.length; i++) {
                sum = sum + +data[i].price * +data[i].amount;
                amount = amount + +data[i].amount;
            }

            for (let i = 0; i < data.   length; i++) {
                tableHtml += '<a class="dropdown-item" href="#">'
                    + data[i].name + ' ' +
                    data[i].price + ' ' +
                    data[i].amount + ' ' +
                    '<img src="/resources/images/delete.png" onclick="deleteProduct(' + data[i].id + '); return false;"/>';
                tableHtml += '</a>';
            }
        }
        tableHtml += '</table>';
        if (data.length === 0) {
        } else {
            $("#sum").html('Count: ' + sum + "$");
            $("#amountOfCart").html(amount);
        }
        $("#cart").html(tableHtml);
    }).fail(function (jqXHR, exception) {
        var msg = '';
        if (jqXHR.status === 0) {
            msg = 'Not connect.\n Verify Network.';
        } else if (jqXHR.status == 404) {
            msg = 'Requested page not found. [404]';
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        } else if (exception === 'parsererror') {
            msg = 'Requested JSON parse failed.';
        } else if (exception === 'timeout') {
            msg = 'Time out error.';
        } else if (exception === 'abort') {
            msg = 'Ajax request aborted.';
        } else {
            msg = 'Uncaught Error.\n' + jqXHR.responseText;
        }
        alert(msg)
    });
}