



function exit() {
    $.ajax({
        type: 'POST',
        url: '/main',
        data: {
            action: 'exit'
        }
    }).done (function(data) {
        location.reload();
    });

}





function deleteAnswer(id) {
    let a = +$("#answersCount").text();
    a = a - 1;
    if (+a > 0) {
        $("#answersCount").html(a);
    }
    else {
        $("#answersCount").html("");
    }
    $.ajax({
        type: 'POST',
        url: '/contacts',
        data: {
            answer_id: id,
            action: 'delete'
        }
    }).done(function () {
       renderAnswers();
    }).fail(function (jqXHR, exception) {
        let msg = '';
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
    return false;
}
function deleteAllProduct() {
    $.ajax({
        type: 'POST',
        url: '/main',
        data: {
            action: 'deleteAll'
        }
    }).done(function () {
        let tableHtml = "";
        tableHtml += '<a class="dropdown-item" href="#">All removed</a>';
        $("#amountOfCart").html("");
        $("#sum").html('Count: ' + 0 + "$")
        $("#cart").html(tableHtml);
        return false;
    }).fail(function (jqXHR, exception) {
        let msg = '';
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
    return false;
}

function deleteUserOrder(id) {
    let a = +$("#amountOfOrders").text();
    a = a - 1;
    if(+a > 0) {
        $("#amountOfOrders").html(a);
    }
    else {$("#amountOfOrders").html("");}
    $.ajax({
        type: 'POST',
        url: '/main',
        data: {
            order_id: id,
            action: 'deleteOrder'
        }
    }).done(function () {
        getUserOrders();
    });
}


function deleteProduct(id) {
    let a = +$("#amountOfCart").text();
    a = a - 1;
    if(+a > 0) {
        $("#amountOfCart").html(a);
    }
    else {$("#amountOfCart").html("");}
    getData();
    $.ajax({
        type: 'POST',
        url: '/main',
        data: {
            product_id: id,
            action: 'delete'
        }
    }).done(function (data) {
        let tableHtml = "";
        if (data.length === 0) {
            tableHtml += 'All removed';
            $("#cart").html(tableHtml);
            $("#sum").html('Count: ' + 0 + "$")
        } else {
            if (data.length === 0) {
                tableHtml += '<a class="dropdown-item" href="#">Your cart is empty</a>';
            } else {
                let sum = 0;
                let amount = 0;
                for (let i = 0; i < data.length; i++) {
                    sum = sum + +data[i].price * +data[i].amount;
                    amount = amount + +data[i].amount;
                }

                for (i = 0; i < data.length; i++) {
                    tableHtml += '<a class="dropdown-item" href="#">'
                        + data[i].name + ' '+
                        data[i].price + ' '+
                        data[i].amount + ' '+
                        '<img src="/resources/images/delete.png" onclick="deleteProduct(' + data[i].id + '); return false;"/>';
                    tableHtml += '</a>';
                }
            }
            tableHtml += '</table>';
            if (data.length === 0) {
            } else {
                $("#sum").html('Count: ' + sum + "$")
            }
            $("#cart").html(tableHtml);
        }
    }).fail(function (jqXHR, exception) {
        let msg = '';
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
    return false;
}