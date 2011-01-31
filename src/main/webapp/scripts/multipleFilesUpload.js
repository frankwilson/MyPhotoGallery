function uploadFile(file, url) {

    console.log( "uploading file "+ file.name );
    
    if( window.FormData ) {
    	console.log( "FormData is supported" );

    	var fd = new FormData();
    	fd.append("myFile", file);

    	xhr = getSender(url);
    	xhr.send(fd);
    }
    else {
    	console.log( "FormData is not supported" );
	    var reader = new FileReader();
	   
	    reader.onload = function() {
	        var boundary = "xxxxxxxxx";
	        // Устанавливаем заголовки
	        xhr.setRequestHeader("Content-Type", "multipart/form-data, boundary=" + boundary);
	        xhr.setRequestHeader("Cache-Control", "no-cache");

	        // Формируем тело запроса
	        var body = "--" + boundary + "\r\n";
	        body += "Content-Disposition: form-data; name='myFile'; filename='" + file.name + "'\r\n";
	        body += "Content-Type: application/octet-stream\r\n\r\n";
	        body += reader.result + "\r\n";
	        body += "--" + boundary + "--";

	        getSender(url);
	        // Читаем файл
	        reader.readAsBinaryString(file);

            // только для firefox
            xhr.sendAsBinary(body);
	    }
    };
}

function displayFiles(files) {
	jQuery.each(files, function(i, file) {      
        if (!file.type.match(/image.*/)) {
          // Отсеиваем не картинки
          return true;
        }           
        // Создаем элемент li и помещаем в него название, миниатюру и progress bar,
        // а также создаем ему свойство file, куда помещаем объект File (при загрузке понадобится)
        var li = jQuery('<li/>').appendTo(imgList);
        jQuery('<div/>').text(file.name).appendTo(li);
        var img = jQuery('<img/>').appendTo(li);
        jQuery('<div/>').addClass('progress').text('0%').appendTo(li);
        li.get(0).file = file;
     
        // Создаем объект FileReader и по завершении чтения файла, отображаем миниатюру и обновляем
        // инфу обо всех файлах
        var reader = new FileReader();
        reader.onload = (function(aImg) {
            return function(e) {
                aImg.attr('src', e.target.result);
                aImg.attr('width', 150);
                /* ... обновляем инфу о выбранных файлах ... */
            };
        })(img);
     
        reader.readAsDataURL(file);
    });
}

function getSender(url) {
    var xhr = new XMLHttpRequest();    
    
    xhr.upload.addEventListener("progress", function(e) {
        if (e.lengthComputable) {
            var progress = (e.loaded * 100) / e.total;
            /* ... обновляем инфу о процессе загрузки ... */
        }
    }, false);
    
    /* ... можно обрабатывать еще события load и error объекта xhr.upload ... */
  
    xhr.onreadystatechange = function () {
        if (this.readyState == 4) {
            if(this.status == 200) {
              /* ... все ок! смотрим в this.responseText ... */
            }
            else {
              /* ... ошибка! ... */
            }
        }
    };

    xhr.open("POST", url);
    
    return xhr;
}