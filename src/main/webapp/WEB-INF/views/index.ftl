<#ftl encoding="utf-8"/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
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

    </head> <#if user ??>
    <body onload="renderAjax(); return false;">
    <#else>
    <body>
    </#if>
<nav class="navbar navbar-expand-md navbar-light fixed-top bg-light">
    <a class="navbar-brand" href="/main" style="color: darkred;font-size: 2rem;">Electro Boom</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02"
            aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <li class="nav-item active">
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
                <a id ="loginName" class="nav-link dropdown-toggle" role ="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">${user.name}</a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/profile">Profile</a>
                    <a class="dropdown-item" href="" data-toggle="modal" data-target="#myModal2" onclick = "getUserOrders(); return false;">Your orders <span class = "badge badge-dark" id = "amountOfOrders"></span></a>
                    <a class="dropdown-item" href="/contacts" >Messages <span class = "badge badge-dark" id = "answersCount"></span></a>
                    <div class="dropdown-divider"></div>
                    <#if user.role.name == "admin">
                    <a class="dropdown-item" href="/admin" id = "adminpanel">Admin Panel</a>
                    </#if>
            <a class="dropdown-item" onclick="exit()">Exit</a>
                </div>
            </li>
            <li class="nav-item dropdown" style="float:left">
                <a class="nav-link dropdown-toggle" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Cart <span class = "badge badge-dark" id = "amountOfCart"></span>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <div id="cart">
                    </div>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" id="sum"></a>
                    <a class="dropdown-item" data-toggle="modal" data-target="#myModal" onclick="getData(); return false;"> Do order </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" onclick="deleteAllProduct(); return false;">Delete all products</a>
                </div>
            </li>
            <#else> <li class="nav-item">
                <a class="nav-link" href="/login">Login</a>
            </li> </#if>
        </ul>
    </div>
</nav>
<div style="height: 120px;"></div>
<div class="container">
    <div class="row">
        <div class="col-lg-9 col-md-12 col-12">
            <div class="container">
                <div class="row">
                    <div class="col-12 col-md-12 col-lg-12">
                        <h1 style="text-align: center;">Electro Boom
                        </h1>
                    </div>
                    <br>
                    <div class="col-12 col-md-12 col-lg-12">
                        <form class="form-inline" style="justify-content: center;">
                            <input class="form-control" type="search" placeholder="Search products" id="q"
                                   onkeyup="search()">
                        </form>
                    </div>
                </div>
            </div>
            <br>
            <div class="container">
                <div class="col-lg-12 col-md-12 col-12">
                    <div class="shop-items">
                        <div class="container-fluid" id="catalog">
                            <div class="row">
                            <#list products as product>
                                <div class="col-md-4 col-sm-6 col-lg-4">
                                    <div class="item">
                                        <img class="img-responsive" src="${product.img}">
                                        <div class="item-dtls">
                                            <h4>
                                                <a style="font-size: 14px; color: #000000; font-weight: bold;"> ${product.name} </a>
                                                <a style="font-size: 12px; color: #adabab;"> Rating: ${product.rating} (${product.countReviews}) </a>
                                            </h4>
                                            <span class="price lblue" id="special"> ${product.price} $</span>
                                        </div>
                                        <#if user ??>
                                        <div class="ecom bg-black">
                                            <a class="btn" href=""
                                               onclick="doBuying(${product.id},${product.price}); return false;"> Add to
                                                cart</a>
                                            <a class="btn" href="/products/${product.id}"> Info</a>
                                        </div>
                                        </#if>
                                    </div>
                                </div>
                            </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 d-none d-lg-block d-md-none">
            <div class="page-header" id="pageHeaderRandomItems">
                <div style="height: 70px;"></div>

                <h2 id="special">Products
            </h2></div>
            <ul class="list-group" id = "randomItems">
                <#list randomproducts as product>
                    <li class="list-group-item" id ="randomItem">
                        <div class="img"><p style="text-align: center;"><a href="/products/${product.id}"><img class="img-fluid" alt="Responsive image"
                                                                                                               src="/${product.img}" style="border: 2px solid #eee;"></a></p></div>
                        <div class="info" id = "info">
                            <a>${product.name}</a>
                            <span id="special">NEW!</span>
                            <div class="price">
                                <span id="special">${product.price}$</span>
                            </div>
                        </div>
                        <a class="btn" href="/products/${product.id}" id ="infoButton" style="color: black;">Info</a>
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
<!-- Модальное окно -->
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
