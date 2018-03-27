# kvot

Övning för division (och multiplikation)

## Översikt

Halvinteraktiv övning för multiplikationstabellen för att användas
tillsammans med övning av kort multiplikation

## Kom igång

För att få en interaktiv utvecklingsmiljö, kör:

    lein figwheel

och peka browsern till [localhost:3449](http://localhost:3449/).
Kolla in [figwheel](https://github.com/bhauman/lein-figwheel) för mer
detaljer kring hur den här utvecklingsmiljön funkar.

För att rensa allt:

    lein clean

För att bygga något att "lägga upp"

    lein do clean, cljsbuild once min

(Går att se i) `resources/public/index.html` (utan live-reloading eller REPL).

## License

Kolla [LICENSE](LICENSE)