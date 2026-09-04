Add-Type -AssemblyName System.Drawing
function Resize-Image($file, $outSize) {
    $path = Resolve-Path $file
    $img = [System.Drawing.Image]::FromFile($path)
    $bmp = New-Object System.Drawing.Bitmap($outSize, $outSize)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($img, 0, 0, $outSize, $outSize)
    $g.Dispose()
    $img.Dispose()
    $newPath = $path.Path.Replace(".png", "_new.png")
    $bmp.Save($newPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    Move-Item -Force $newPath $path
}
Resize-Image "src/main/resources/assets/mwc/textures/items/turret_base.png" 64
Resize-Image "src/main/resources/assets/mwc/textures/items/vest_render.png" 64
