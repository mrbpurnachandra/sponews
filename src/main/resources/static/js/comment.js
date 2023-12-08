$(document).ready(function () {
    const commentForm = $("#commentForm");
    const container = $("#comment-container");

    if (commentForm) {
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

$(document).ready(() => loadComments(0))

function loadComments(page) {
    const container = $("#comment-container");
    const id = container.attr("data-id");

    unrenderLoadMoreButton(container);

    $.ajax({
        type: "GET",
        url: `http://localhost:8080/article/${id}/comment?page=${page}`,
        success(response) {
            response.data.forEach(comment => render(container, comment));
            if (response.hasNext) renderLoadMoreButton(container, page + 1);
        },
        error(err) {
            console.log(err);
        }
    })
}

function render(container, comment, top = false) {
    const innerContent = `
        <div class="card" style="max-width: 560px">
            <div class="card-body">
                <div class="my-2">
                    <span>${generateEmoji(comment.emotion)}</span>
                </div>
                <div class="card-text">${comment.content}</div>
                <div class="card-text mt-2">
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

function generateEmoji(emotion) {
    switch (emotion) {
        case "HAPPY":
            return "☺️☺️☺️☺️️"
        case "SAD":
            return "💔💔💔💔"
        default:
            return "❓"
    }
}

function renderLoadMoreButton(container, page) {
    const button = `
        <button class="btn btn-link" onclick="loadComments(${page})">थप लोड गर्नुहोस्</button>
    `;

    const elem = document.createElement("div");
    elem.innerHTML = button;
    elem.className = "mt-4";
    elem.id = "load-more-button";

    container.append(elem);
}

function unrenderLoadMoreButton() {
    $("#load-more-button").remove();
}