@echo off
set "origen=E:\Windows\programas\Proyectos\Minecraft\PaintKraft\PaintKraft\build\libs\"
set "destino=Z:\Desarrollo\PaintKraft - Rework\plugins\"

rem Copia todos los archivos que comiencen con 'PaintKraft' y terminen en '.jar'
xcopy "%origen%\PaintKraft*.jar" "%destino%" /Y

echo Copia completada.

