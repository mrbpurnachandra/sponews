$(document).ready(function () {
    console.log("Document is Ready");
    const commentForm = $("#commentForm");

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
                success: function (response) {
                    console.log(response)
                },
                error: function (error) {
                    contentErrorElement.removeClass("visually-hidden");
                }
            });
        })
    }
});