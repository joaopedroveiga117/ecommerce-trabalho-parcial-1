
INSERT INTO categoria (id, nome, descricao) VALUES
(1, 'Perifericos', 'Acessorios para computador'),
(2, 'Hardware', 'Componentes internos'),
(3, 'Monitores', 'Telas e displays'),
(4, 'Audio', 'Produtos de som'),
(5, 'Armazenamento', 'Dispositivos de armazenamento');


INSERT INTO produto (id, nome, descricao, estoque, preco, categoria_id) VALUES
(1, 'Mouse Gamer', 'Mouse com alta precisao', 20, 120.00, 1),
(2, 'Teclado Mecanico', 'Teclado com switches mecanicos', 15, 250.00, 1),
(3, 'SSD 1TB', 'SSD de alta velocidade', 10, 450.00, 5),
(4, 'Monitor 24', 'Monitor Full HD', 8, 899.90, 3),
(5, 'Headset Gamer', 'Headset com microfone', 12, 199.90, 4);


INSERT INTO cliente (id, nome, email, telefone) VALUES
(1, 'Joao Silva', 'joao@email.com', '43999990001'),
(2, 'Maria Souza', 'maria@email.com', '43999990002'),
(3, 'Carlos Lima', 'carlos@email.com', '43999990003'),
(4, 'Ana Oliveira', 'ana@email.com', '43999990004'),
(5, 'Pedro Santos', 'pedro@email.com', '43999990005');


INSERT INTO pedido (id, data, status, valor_total, cliente_id) VALUES
(1, '2026-09-01 10:00:00', 'PAGO', 120.00, 1),
(2, '2026-09-02 11:30:00', 'PENDENTE', 250.00, 2),
(3, '2026-09-03 14:20:00', 'PAGO', 450.00, 3),
(4, '2026-09-04 09:15:00', 'ENVIADO', 899.90, 4),
(5, '2026-09-05 16:40:00', 'PAGO', 199.90, 5);


INSERT INTO item_pedido (id, quantidade, valor_unitario, pedido_id, produto_id) VALUES
(1, 1, 120.00, 1, 1),
(2, 1, 250.00, 2, 2),
(3, 1, 450.00, 3, 3),
(4, 1, 899.90, 4, 4),
(5, 1, 199.90, 5, 5);

INSERT INTO pagamento (id, valor, data, status, tipo, pedido_id) VALUES
(1, 120.00, '2026-09-01 10:05:00', 'APROVADO', 'PIX', 1),
(2, 250.00, '2026-09-02 11:35:00', 'PENDENTE', 'BOLETO', 2),
(3, 450.00, '2026-09-03 14:25:00', 'APROVADO', 'CARTAO', 3),
(4, 899.90, '2026-09-04 09:20:00', 'APROVADO', 'CARTAO', 4),
(5, 199.90, '2026-09-05 16:45:00', 'APROVADO', 'PIX', 5);