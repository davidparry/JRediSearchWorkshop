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
            //var table = [["Score", "Chapter", "Line", "ID", "Text"]];
            var table = [];
            if (data.msg === 'found') {
                var items = data.results.list;
                for (var i = 0; items.length > i; i++) {
                    var item = items[i];
                    table.push([item.score, item.chapter, item.line, item.id, item.text, item.title])
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
        var table = $("<table/>").addClass('table table-bordered');
        var headers = ["Score", "Chapter", "Line", "ID", "Text"];
        var heads = $("<tr/>");
        $.each(headers, function (colIndex, c) {
            heads.append($("<th/>").text(c).addClass('table-bordered thead td bg-primary'));
        });
        table.append(heads);


        $.each(data, function (rowIndex, r) {
            var row = $("<tr/>");
            var colorCode = "bg-success";
            if (rowIndex % 2 == 0) {
                colorCode = 'bg-success';
            } else {
                colorCode = 'table-two-color';
            }
            var tooltip = '';
            $.each(r, function (colIndex, c) {
                if (colIndex == 5) {
                    tooltip = c;
                } else {
                    row.append($("<td/>").text(c)).addClass('table-bordered td ' + colorCode);
                }
            });

            row.mouseover(function () {
                $('#popup').html('<b>&nbsp;TITLE:&nbsp;</b>&nbsp;<i>'+ tooltip+ '</i>').show();
            });
            row.mouseout(function () {
                $('#popup').html('').hide();
            });


            table.append(row);
        });
        return container.append(table);
    }


}