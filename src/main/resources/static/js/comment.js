$(document).ready(function () {
    const commentForm = $("#commentForm");
    const container = $("#comment-container");

    if(commentForm) {
        commentForm.on("submit", function (event) {
            event.preventDefault();

            const contentErrorElement = $("#content-error");

            const formData = new FormData(event.target);
            const data = Object.fromEntries(formData);

            $.ajax({
                type: "POST",
                url: `http://localhost:8080/article/${data["article.id"]}/comment`,
                data: data,
                success: function (comment) {
                    render(container, comment, true);
                    commentForm.get(0).reset();
                },
                error: function (error) {
                    contentErrorElement.removeClass("visually-hidden");
                }
            });
        })
    }
});

$(document).ready(function () {
    const container = $("#comment-container");
    const id = container.attr("data-id");

    $.ajax({
        type: "GET",
        url: `http://localhost:8080/article/${id}/comment`,
        success(comments) {
            comments.forEach(comment => render(container, comment));
        },
        error(err) {
            console.log(err);
        }
    })
})

function render(container, comment, top = false) {
    const innerContent = `
        <div class="card" style="max-width: 560px">
            <div class="card-body">
                <div class="card-text">${comment.content}</div>
                <div class="card-text">
                    <small class="text-body-secondary">${comment.name}</small>
                </div>
            </div>  
        </div>
    `;

    const elem = document.createElement("div");
    elem.innerHTML = innerContent;
    elem.style.marginTop = "15px";

    top ? container.prepend(elem) : container.append(elem);
}