// static/js/navbar.js
document.addEventListener("DOMContentLoaded", function () {
    try {
        const links = document.querySelectorAll(".navbar-menu a");
        links.forEach(link => {
            if (link.href === window.location.href) {
                link.classList.add("active");
            }
        });
    } catch (e) {
        console.warn("Navbar JS error:", e);
    }
});
