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
            <li class="nav-item active">
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
            <div style="height: 600px">
                <div class="raw">
                    <div class="col-lg-10 col-md-12 col-12">
                        <h1 style="text-align: center;" id ="special">${product.name}
                        </h1>
                    </div>
                    <div class="col-lg-10 col-md-12 col-12">
                        <div class="list-group-item" style="padding: 5%;">
                            <p style="font-size: 60px; color: #000000; font-weight: bold;"> ${product.name} </p>
                            <img class="img-responsive" src="/${product.img}">
                            <p style="font-size: 15px;"> ${product.category}</p>
                            <p class="price lblue" id="special"
                               style="font-size: 40px"> ${product.price} $</p>

                            <br>
                            <h3>About product</h3>
                            <p style="word-wrap: break-word">
                                ${product.about}
                            </p>
                            <br>
                            <a class="btn" href="" style="color: black"
                               onclick="doBuying(${product.id},${product.price}); return false;"> Add to
                                cart</a>
                            <a class="btn" href="/main" style="color: black;">Back to catalog</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-3 d-none d-lg-block d-md-none">
            <div class="page-header">
                <h2 id="special">
                    Other products
                </h2></div>
            <ul class="list-group">
                <#list products as product>
                <li class="list-group-item">
                    <div class="img"><a href="#"><img class="img-fluid" alt="Responsive image"
                                                      src="/${product.img}"></a></div>
                    <div class="info">
                        <a>${product.name}</a>
                        <span id="special">NEW!</span>
                        <div class="price">
                            <span id="special">${product.price}$</span>
                        </div>
                    </div>
                    <a class="btn-black" href="/products/${product.id}" style="color: black;">Info</a>
                </li>
                </#list>
            </ul>
        </div>
    </div>
        <#if user ??>
        <div class="col-12 col-lg-6 col-md-12">
        </#if>
</div>
</div>
<br>
<footer class="py-5 bg-dark">
    <div class="container">
        <p class="m-0 text-center text-white">Electro Boom</p>
    </div>
</footer>
</body>
</html>