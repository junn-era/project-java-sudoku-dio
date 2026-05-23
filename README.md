# 🎮 Sudoku - Desafio DIO Globant Bootcamp

Um simulador de jogo de Sudoku desenvolvido em **Java**, com interface textual e validações robustas para entrada de dados.

## 📋 Sobre o Projeto

Este projeto implementa um jogo de tabuleiro sudoku 9x9 com as seguintes funcionalidades:

- ✅ Iniciar um novo jogo com tabuleiro pré-configurado
- ✅ Inserir números em posições vazias
- ✅ Remover números inseridos
- ✅ Visualizar o tabuleiro atual
- ✅ Verificar status do jogo
- ✅ Limpar o tabuleiro
- ✅ Finalizar o jogo

### 🛡️ Validações Implementadas

O projeto inclui validações robustas para entrada de dados:

- **Entrada não-numérica**: Se o usuário digita texto, símbolos ou qualquer coisa que não seja um número inteiro, o programa exibe a mensagem:
  ```
  Entrada inválida. Informe um número inteiro.
  ```

- **Intervalo de valores**: O programa valida se os números estão dentro do intervalo permitido (por exemplo, 1-8 para o menu, 0-8 para linha/coluna, 1-9 para valores do sudoku):
  ```
  Informe um número entre X e Y
  ```

- **Sem travamento**: O programa nunca lança `InputMismatchException`. Simplesmente solicita a entrada novamente até que seja válida.

## 📁 Estrutura do Projeto

```
sudoku/
├── src/
│   └── br/com/dio/
│       ├── Main.java              # Classe principal com lógica do jogo
│       ├── model/
│       │   ├── Board.java          # Representa o tabuleiro
│       │   ├── Space.java          # Representa uma célula do tabuleiro
│       │   └── GameStatusEnum.java # Estados possíveis do jogo
│       └── util/
│           └── BoardTemplate.java  # Template visual do tabuleiro
├── README.md                       # Este arquivo
└── sudoku.iml                      # Arquivo de configuração IntelliJ
```

## 🚀 Como Compilar e Executar

### Pré-requisitos
- **Java 17+** (compilado com JDK-21)
- Terminal/CMD ou PowerShell

### Compilação

```bash
# Navegue até a raiz do projeto
cd sudoku

# Compile todas as classes
javac -d out src/br/com/dio/*.java src/br/com/dio/model/*.java src/br/com/dio/util/*.java
```

### Execução

```bash
# Execute o programa (sem dados iniciais)
java -cp out br.com.dio.Main

# Execute com um tabuleiro pré-configurado
java -cp out br.com.dio.Main "0,0;4,0,1;6,0,2;8,0,3;0,0,4;..."
```

**Formato dos dados iniciais**: `valor,coluna,linha;valor,coluna,linha;...`
- `valor`: O número (0 = célula vazia, 1-9 = número fixo)
- `coluna`: Posição de coluna (0-8)
- `linha`: Posição de linha (0-8)

## 🎯 Como Jogar

1. **Inicie o programa** com dados do sudoku (veja "Execução" acima)
2. **Selecione uma opção** do menu:
   ```
   Selecione uma das opções a seguir:
   1 - Iniciar um novo jogo
   2 - Colocar um novo número
   3 - Remover um número
   4 - Visualizar jogo atual
   5 - Verificar status do jogo
   6 - Limpar jogo
   7 - Finalizar jogo
   8 - Sair
   ```

3. **Inserir número** (opção 2):
   - Digite a coluna (0-8)
   - Digite a linha (0-8)
   - Digite o número (1-9)

4. **Remover número** (opção 3):
   - Digite a coluna (0-8)
   - Digite a linha (0-8)

5. **Visualizar jogo** (opção 4):
   - Vê o estado atual do tabuleiro em formato visual

## 💡 Exemplos de Teste

### Teste 1: Entrada inválida no menu
```
Entrada: "abc" → Resposta: "Entrada inválida. Informe um número inteiro."
```

### Teste 2: Número fora do intervalo
```
Entrada: "9" (no menu que permite 1-8) → Resposta: "Informe um número entre 1 e 8"
```

### Teste 3: Decimal inválido
```
Entrada: "1.5" → Resposta: "Entrada inválida. Informe um número inteiro."
```

## 🏗️ Arquitetura

### Classes Principais

#### `Main.java`
- **responsabilidade**: Gerenciar o fluxo do jogo e interação com o usuário
- **métodos principais**:
  - `main(String[] args)`: Ponto de entrada, carrega tabuleiro e menu
  - `startGame(Map<String, String> positions)`: Inicializa um novo jogo
  - `inputNumber(Scanner scanner)`: Permite inserir um número
  - `removeNumber(Scanner scanner)`: Remove um número
  - `showCurrentGame()`: Exibe o tabuleiro
  - `runUntilGetValidNumber(int min, int max)`: **Valida entrada do usuário com tratamento de exceções**

#### `Board.java`
- Representa o tabuleiro 9x9
- Armazena a lista de espaços (células)

#### `Space.java`
- Representa uma célula individual do sudoku
- Armazena o valor esperado, valor atual (inserido pelo jogador) e se é fixo

#### `BoardTemplate.java`
- Define o template visual do tabuleiro em formato textual

#### `GameStatusEnum.java`
- Define os possíveis estados do jogo (em desenvolvimento)

## 🔧 Modificações Recentes

### Validação Robusta de Entrada
A versão atual corrigiu um problema crítico:

**Antes**:
```java
var current = scanner.nextInt();  // Lança InputMismatchException se input não é inteiro
```

**Depois**:
```java
while (true) {
    var token = scanner.next();
    try {
        var current = Integer.parseInt(token);
        if (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            continue;
        }
        return current;
    } catch (NumberFormatException ex) {
        System.out.println("Entrada inválida. Informe um número inteiro.");
    }
}
```

### Parsing de Argumentos
Corrigido o parsing dos argumentos de entrada para aceitar o formato `valor,coluna,linha` e montar corretamente o mapa de posições.

## 📊 Formato Visual do Tabuleiro

O tabuleiro é exibido em um formato visual amigável:

```
*|---0---||---1---||---2---||*|---3---||---4---||---5---||*|---6---||---7---||---8---|*
*|       ||       ||       ||*|       ||       ||       ||*|       ||       ||       |*
0|   4   ||   6   ||   8   ||*|       ||       ||       ||*|       ||       ||       |0
*|       ||       ||       ||*|       ||       ||       ||*|       ||       ||       |*
```

- Linhas em branco indicam células vazias
- Números aparecem no centro de cada célula
- Divisórias `*` separam os blocos do sudoku (3x3)

## 🐛 Possíveis Melhorias Futuras

- [ ] Implementar validação completa de sudoku (linhas, colunas, blocos)
- [ ] Adicionar feedback ao tentar inserir número inválido
- [ ] Implementar sistema de dificuldade
- [ ] Interface gráfica (Swing/JavaFX)
- [ ] Salvar/carregar jogo
- [ ] Sistema de hints/dicas

## 👨‍💻 Autor

Desenvolvido como parte do **Desafio DIO Globant Bootcamp** - Trilha de Desenvolvimento Java

## 📝 Licença

Este projeto é fornecido como material educacional.

---

**Última atualização**: Maio 2026  
**Estado**: Funcional com validações robustas ✅

