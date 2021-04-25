$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/api/cache?key=jack"
    }).then(function(data) {
       $('.success').append(data.success);
       $('.code').append(data.code);
       $('.key').append(data.data.key);
       $('.value').append(data.data.value);
       console.log(data);
    });
});