<#ftl encoding="utf-8"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="shortcut icon"
          href="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFzPTtaVLS7029u35BpMoJP-7RdfA8GH3mCD50ge12uD2XXTwi"
          type="image/x-icon">
    <meta charset="UTF-8">
    <title>Electro Boom</title>
    <script src="/resources/js/jquery-ui-1.12.1/external/jquery/jquery.js"></script>
    <script src="/resources/js/jquery-ui-1.12.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="/resources/js/jquery-ui-1.12.1/jquery-ui.css">
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/style.css">
    <script src="/resources/js/bootstrap/bootstrap.min.js"></script>
    <script src="/resources/js/adding.js"></script>
    <script src="/resources/js/deleting.js"></script>
    <script src="/resources/js/getData.js"></script>
    <script src="/resources/js/renderAjax.js"></script>
</head>
<#if user ??>
    <body onload="renderAjax(); return false;">
<#else>
    <body>
</#if>
<nav class="navbar navbar-expand-md navbar-light fixed-top bg-light">
    <a class="navbar-brand" href="/main" style="color: darkred; font-size: 2rem;">Electro Boom</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02"
            aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <li class="nav-item">
                <a class="nav-link" href="/main">Catalog <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/contacts">Contacts</a>
            </li>
            <li class="nav-item">
                <a class="nav-link disabled" href="#">About us</a>
            </li>
            <#if user ??>
            <li class="nav-item dropdown">
                <a id="loginName" class="nav-link dropdown-toggle" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">${user.name}</a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/profile">Profile</a>
                    <a class="dropdown-item" href="" data-toggle="modal" data-target="#myModal2"
                       onclick="getUserOrders(); return false;">Your orders <span class="badge badge-dark"
                                                                                  id="amountOfOrders"></span></a>
                    <a class="dropdown-item" href="/contacts">Messages <span class="badge badge-dark"
                                                                             id="answersCount"></span></a>
                    <div class="dropdown-divider"></div>
                    <#if user.role.name == "admin">
                    <a class="dropdown-item" href="/admin" id="adminpanel">Admin Panel</a>
                    </#if>
                    <a class="dropdown-item" href="/main" onclick="exit()">Exit</a>
                </div>
            </li>
            <li class="nav-item dropdown" style="float:left">
                <a class="nav-link dropdown-toggle" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Cart <span class="badge badge-dark" id="amountOfCart"></span>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <div id="cart">
                    </div>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" id="sum"></a>
                    <a class="dropdown-item" data-toggle="modal" data-target="#myModal"
                       onclick="getData(); return false;"> Do order </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" onclick="deleteAllProduct(); return false;">Delete all products</a>
                </div>
            </li>
            <#else>
                <li class="nav-item">
                    <a class="nav-link" href="/login">Login</a>
                </li> </#if>
        </ul>
    </div>
</nav>
<div style="height: 120px;"></div>
<div class="container">
    <div class="row">
        <div class="col-lg-9 col-md-12 col-12">
            <div>
                <div class="raw">
                    <div class="col-lg-10 col-md-12 col-12">
                        <h1 style="text-align: center;" id="special">${product.name}
                        </h1>
                    </div>
                    <div class="col-lg-10 col-md-12 col-12">
                        <div class="list-group-item" id ="productItem">
                            <p style="font-size: 60px; color: #000000; font-weight: bold;">${product.name}<span
                                    style="font-size: 15px;">(${product.category})</span></p>
                            <img class="img-responsive" id="productImg" src="/${product.img}">
                            <p class="price lblue" id="rise"
                               style="font-size: 40px"> ${product.price} $</p>
                            <div>
                                <h3 id="about">About product</h3>
                                <p style="word-wrap: break-word">
                                ${product.about}
                                </p>
                            </div>
                            <div id="blockButtons">
                             <#if user ??>
                            <a class="btn" href="" style="color: darkred"
                               onclick="doBuying(${product.id},${product.price}); return false;"> Add to
                                cart</a>
                             </#if>
                                <a class="btn" href="/main" style="color: darkred;">Back to catalog</a>
                                <a class="btn" onclick="openFeedback()" style="color: darkred;">Add feedback</a>
                            </div>
                            <form id="formFeedback" style="display: none;">
                                <div class="form-group">
                                    <label for="exampleFormControlTextarea1">Feedback</label>
                                    <textarea name="text" class="form-control" id="exampleFormControlTextarea1"
                                              rows="3"></textarea>
                                </div>
                    <#if user ??>
                    <button class="btn btn-lg btn-black btn-block" type="submit"
                            onclick="sendingFeedback(); return false;">Send
                    </button>
                    <#else>
                    <button class="btn btn-lg btn-primary btn-danger" type="submit" onclick="">Please login first
                    </button>
                    </#if></form>
                            <br>
                        <#--<div class="container" id="answers">-->
                        <#--<ul class="list-group">-->
                        <#--<#list feedbacks as a>-->
                        <#--<li class="list-group-item">-->
                        <#--<div class="alert alert-light" role="alert"><h5> ${a.owner} </h5>-->
                        <#--<h5 id="special">${a.feedback} </h5>-->
                        <#--</div>-->
                        <#--</li>-->
                        <#--</#list>-->

                        <#--</ul>-->
                        <#--</div>-->
                            <div class="container" id="answers">
                                <ul class="list-group">
                                    <li class="list-group-item">
                                        <div class="alert alert-light" role="alert"><h5 class = "nickname"> Nastya </h5>
                                            <h5 id="special"> Normal </h5>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 d-none d-lg-block d-md-none">
            <div class="page-header" id = "pageHeaderRandomItems">
                <h2 id="special">Products
                </h2></div>
            <ul class="list-group" id = "randomItems">
                <#list products as product>
                    <li class="list-group-item" id ="randomItem">
                        <div class="img"><p style="text-align: center;"><a href="/products/${product.id}"><img class="img-fluid" alt="Responsive image"
                                                          src="/${product.img}" style="border: 2px solid #eee;  "></a></p></div>
                        <div class="info" id = "info">
                            <a>${product.name}</a>
                            <span id="special">NEW!</span>
                            <div class="price">
                                <span id="special">${product.price}$</span>
                            </div>
                        </div>
                        <a class="btn" href="/products/${product.id}" id = "infoButton" style="color: black;">Info</a>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</div>
<br>
<footer class="py-5 bg-dark">
    <div class="container">
        <p class="m-0 text-center text-white">Electro Boom</p>
    </div>
</footer>
<#if user ??>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">Your order</h4>
            </div>
            <div class="modal-body" id = "cart2">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="doOrder(); return false;" id ="dobutton">Do</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">Your order</h4>
            </div>
            <div class="modal-body" id = "orders">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
</#if>
</body>
</html>