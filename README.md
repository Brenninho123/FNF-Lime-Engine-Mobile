<img width="1536" height="864" alt="Lime Engine Logo" src="https://github.com/user-attachments/assets/758cc280-8202-4261-b40a-701fd9a08aff" />

# FNF-Lime-Engine
Essa É a primeira Engine De FNF Brasileira.
A Lime Engine É Uma Engine de FNF Legacy que é focada na simplicidade e pouco esforço para fazer o seu mod com exelentes performances e quase sem limites, graças a lib polymod, que fornece o .hxc (Haxe Scripted Class) Não sabe programar hxc? leia abaixo, e aprenda.

A Lime Engine utiliza o formato `.hxc` (Haxe Scripted Class), que segue o mesmo padrão oficial da biblioteca Polymod. ou seja, se você já fez um mod pra v-slice, você já pode fazer TUDO no seu mod de lime engine, sem precisar da recompilação do código-fonte, mas mesmo assim a recompilação continua 100% opçional

## 📌 Estrutura Básica de um Script

Diferente do Lua, no `.hxc` você precisa importar as classes, definir uma classe com o mesmo nome do arquivo e estender a classe mãe correta (como `Song`, `Module`, `State`, `Stage`, `SubState`, etc.).

Aqui está um exemplo básico de script para interceptar eventos de uma música (salve como `ExemploScript.hxc`):

```
// 1. Sempre faça os imports necessários no topo do arquivo
import funkin.modding.module.Module;
import funkin.play.PlayState;
import funkin.play.components.HealthIcon;

// 2. A classe DEVE ter exatamente o mesmo nome do arquivo físico
class ExemploScript extends Module
{
    // O construtor da classe
    public function new()
    {
        // Define o ID único do seu módulo/script
        super("meu-script-customizado");
    }

    // Função executada quando uma música começa no PlayState
    override function onSongStart(event:SongStartEvent):Void
    {
        super.onSongStart(event);
        
        // Exemplo: Mandar uma mensagem no terminal de debug do jogo
        trace("A música começou com sucesso via Lime Engine!");
    }

    // Função executada a cada frame do jogo
    override function onUpdate(elapsed:Float):Void
    {
        super.onUpdate(elapsed);

        // Seu código de lógica por frame entra aqui
    }
}
```

## 💡 Dicas Importantes para Modders

1. **Maiúsculas e Minúsculas:** O nome da classe dentro do código precisa ser idêntico ao nome do arquivo `.hxc` (incluindo letras maiúsculas).
2. **Uso do `super`:** Sempre chame as funções originais usando `super.nomeDaFuncao()` para garantir que o comportamento padrão do jogo não quebre.
3. **Tipagem:** O Haxe aceita tipagem estática (ex: `elapsed:Float`), o que ajuda a evitar erros bobos de digitação e melhora a performance.

Se você não conseguiu aprender (o que eu acho que provavelmente aconteceu) Não Esquenta, visite a nossa [WIKI]()e aprenda (De Verdade).

A lime engine foi feita no HaxeFlixel


O Novo FNF V-slice também é legal, porque não [Joga](https://ninja-muffin24.itch.io/funkin/download/eyJpZCI6NzkyNzc4LCJleHBpcmVzIjoxNzg0MzMzMzU0fQ%3d%3d%2e939%2ffqMfdkGneiU%2fwR%2bVM74svoo%3d)?

Lime Engine por [GlitchMaster](https://github.com/glitch781212master-mist) & [JoãoBanana](https://github.com/JoaoBanana78060-pao), Friday Night Funkin' por ninjamuffin99
