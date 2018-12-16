$(document).ready(function () {

    $("#search-form").submit(function (event) {
        event.preventDefault();
        search_submit();
    });


    $("#term").autocomplete({
        minChars: 2,
        noCache: true,
        serviceUrl: '/api/partial',
        paramName: 'partialTerm',
        dataType: 'json',
        onSelect: suggest
    });


});

function suggest(suggestion) {
    search_submit()
}


function search_submit() {

    var search = {};
    search["term"] = $("#term").val();

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/search",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $('#feedback').empty();
            var table = [["Score", "Chapter", "Line", "ID", "Text"]];
            if (data.msg === 'found') {
                var items = data.results.list;
                for (var i = 0; items.length > i; i++) {
                    var item = items[i];
                    table.push([item.score, item.chapter, item.line, item.id, item.text])
                }
            } else {
                table.push(['Nothing found', '', '', '', '', ''])
            }
            makeTable($('#feedback'), table);
            console.log("SUCCESS : ", table);
            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {
            $('#feedback').empty();
            var json = "<h4>Search Error Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

    function makeTable(container, data) {
        var table = $("<table/>").addClass(['table']);
        $.each(data, function (rowIndex, r) {
            var row = $("<tr/>");
            $.each(r, function (colIndex, c) {
                row.append($("<t" + (rowIndex == 0 ? "h" : "d") + "/>").text(c));
            });
            table.append(row);
        });
        return container.append(table);
    }


}