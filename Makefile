.PHONY: clean sass build font replace-time
sass:
	npm run build:css
prepare-dev:
	npm install .
	lein cljsbuild once electron-dev
clean:
	lein clean
build:  sass
	lein do clean, cljsbuild once frontend-release, cljsbuild once electron-release
	electron-packager . electron-app --platform=linux  --arch=x64 --electron-version=6.0.12 --overwrite --download.mirrorOptions.mirror=https://npm.taobao.org/mirrors/electron/
	electron-packager . electron-app --platform=win32  --arch=x64 --overwrite --download.mirrorOptions.mirror=https://npm.taobao.org/mirrors/electron/
dev-sass:
	npm run watch:sass
