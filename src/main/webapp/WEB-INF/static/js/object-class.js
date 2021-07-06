$(function () {
    $('#object-class-tree')
        .on("changed.jstree", function (e, data) {
            if (data.selected.length > 0) {
                objectClassPropertyUrl = objectClassUrl + '/' + data.selected + '/properties';
                propertyTable.ajax.url(treeDataUrl + '/' + data.selected + '/properties');
                propertyTable.ajax.reload();
            }

        })
        .jstree({
            core: {
                data: {
                    url: function (node) {
                        return treeDataUrl;
                    },
                    data: function (node) {
                        return {
                            'id': node.id
                        };
                    }
                },
                themes: {
                    name: 'proton',
                    responsive: true
                }
            },
            "plugins": [
                "contextmenu", "dnd", "search",
                "state", "types", "wholerow"
            ]
        });

    propertyTable = $('#paramTable').DataTable({
        ajax: {
            url: '/webService/api/v1/objectClasses/0/properties'
        },
        rowId: 'id',
        drawCallback: function (settings) {
            MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
            $('.propertyId').on("click", function (evt) {
                alert(JSON.stringify(evt));
            });
        },
        columns: [
            {
                title: 'id',
                data: 'id',
                width: '4px'
            },
            {
                title: 'Наименование',
                data: 'property.name',
                width: '5rem'
            },
            {
                title: 'Норматив',
                data: 'standard',

                width: '5rem'
            },
            {
                title: 'Ед. Изм.',
                data: 'property.unit.name',
                width: '5rem'
            },
            {
                title: 'Н.Д. На методы исследования',
                data: 'property.unit.name',
                width: '5rem'
            },
            {
                title: 'Н.Д. На оценку',
                data: null,
                width: '5rem'
            },
            {
                title: "",
                data: null,
                width: "4px",
                className: "dt-center editor-edit align-middle",
                render: function (data, type, row, meta) {
                    // return '<a href="' + objectClassPropertyUrl + '/' + data.id + '" class="text-info bi bi-pencil h4" />'
                    return '<a href="/webService/private/objectClassProperties/' + data.id + '" class="text-info bi bi-pencil h4" />'
                },
                orderable: false
            },
            {
                title: "",
                data: null,
                width: "4px",
                className: "dt-center editor-delete align-middle",
                render: function (data, type, row, meta) {
                    // return '<a href="' + objectClassPropertyUrl + '/' + data.id + '" onclick="return DeleteFunction(\'Удалить документ?\');" class="text-danger bi bi-trash h4"></a>'
                    return '<a href="/webService/private/objectClassProperties/' + data.id + '/delete" onclick="return DeleteFunction(\'Удалить показатель?\');" class="text-danger bi bi-trash h4"/>'
                },
                orderable: false
            }
        ],
        dom: "<'row'<'toolbar col-sm-12 col-md-6'><'col-sm-12 col-md-6'fb>>"
            + "<'row'<'col-sm-12'tr>>"
            + "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-2'l><'col-sm-12 col-md-5'p>>",
        columnDefs: [{
            targets: '_all',
            defaultContent: ""
        }],
    });

    $('div.toolbar').html('<a class="btn btn-outline-dark btn-sm bi bi-person-plus editor-add" href="#">&nbsp;Создать</a>');
    $('div.toolbar .editor-add').on('click', function () {
        let objectClassId = $('#object-class-tree').jstree('get_selected')[0];
        if (objectClassId != undefined)
            $(this).attr('href', '/webService/private/objectClassProperties/new?objectClassId=' + objectClassId);
    })
    // $('a.editor-add').on('click', editModal);

    var modal = $('#modalDialog');
    var modalBody = modal.find('.modal-body');
    var modalFooter = modal.find('.modal-footer');
    var modalSubmitButon = modalFooter.find('#submit-button');
    var modalForm;
    var propUrl;
    modalSubmitButon.on('click', function (e) {
        e.preventDefault();
        modalForm = modalBody.find('form');
        $.ajax({
            url: modalForm.attr('action'),
            type: modalForm.attr('method'),
            data: modalForm.serialize()
        }).done(function (result) {
            modal.modal('hide');
            propertyTable.ajax.reload();
        }).always(function (result, status, error) {
            modalBody.html(result.responseText);
        });
    });

    function editModal(event) {
        event.preventDefault();
        let object = $(this);
        let href;
        if (object.is('a'))
            href = object.attr('href');
        else
            href = object.find('a').attr('href')
        console.log(href);
        modalBody.load(href, function () {
            modal.modal('show');
        });
    }

    // propertyTable.on('click', 'td.editor-edit', editModal);
    // propertyTable.on('click', 'td.editor-delete', function (e) {
    //     e.preventDefault();
    //     $.ajax({
    //         url: $(this).find('a').attr('href'),
    //         type: 'DELETE'
    //     }).always(function () {
    //         propertyTable.ajax.reload();
    //     })
    // });
});