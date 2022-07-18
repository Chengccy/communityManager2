<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ajax 文件导出</title>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<button type="button" onclick="exportExcel()">导出</button>

<script type="text/javascript">
    function exportExcel() {
        axios({
            method: 'POST',
            url: "userServlet?action=test",
            timeout: 5000,
            responseType: 'blob'
        }).then(function (res) {
            var data = res.data;
            var blob = new Blob([data], {type: 'application/octet-stream'});
            var url = URL.createObjectURL(blob);
            var exportLink = document.createElement('a');
            exportLink.setAttribute("download","文件下载.xls");
            exportLink.href = url;
            document.body.appendChild(exportLink);
            exportLink.click();
        })
    }
</script>
</body>
</html>