// Apply the saved theme right away (dark by default) so the page doesn't flash.
(function () {
  var saved = null;
  try { saved = localStorage.getItem("theme"); } catch (e) {}
  document.documentElement.dataset.theme = saved === "light" ? "light" : "dark";
})();
 
document.addEventListener("DOMContentLoaded", function () {
  var toggle = document.getElementById("theme-toggle");
  if (!toggle) return;
 
  function sync() {
    var isDark = document.documentElement.dataset.theme === "dark";
    toggle.setAttribute("aria-checked", String(isDark));
  }
 
  toggle.addEventListener("click", function () {
    var next = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
    document.documentElement.dataset.theme = next;
    try { localStorage.setItem("theme", next); } catch (e) {}
    sync();
  });
 
  sync();
});