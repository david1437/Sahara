ALTER TABLE shopping_cart
ADD FOREIGN KEY (u_recid) REFERENCES users(u_recid),
ADD FOREIGN KEY (p_recid) REFERENCES products(p_recid),
ADD FOREIGN KEY (pr_recid) REFERENCES producers(pr_recid);

ALTER TABLE purchase_history
ADD FOREIGN KEY (u_recid) REFERENCES users(u_recid),
ADD FOREIGN KEY (p_recid) REFERENCES products(p_recid),
ADD FOREIGN KEY (pr_recid) REFERENCES producers(pr_recid);

ALTER TABLE shipping_type
ADD FOREIGN KEY (ut_recid) REFERENCES user_type(ut_recid);

ALTER TABLE producers DROP COLUMN pri_recid;
ALTER TABLE producers
ADD FOREIGN KEY (pt_recid) REFERENCES producer_type(pt_recid);

ALTER TABLE producer_inventory
ADD FOREIGN KEY (pr_recid) REFERENCES producers(pr_recid),
ADD FOREIGN KEY (p_recid) REFERENCES products(p_recid);

ALTER TABLE products
ADD FOREIGN KEY (pr_recid) REFERENCES producers(pr_recid),
ADD FOREIGN KEY (c_recid) REFERENCES product_category(c_recid);

ALTER TABLE users
ADD FOREIGN KEY (ut_recid) REFERENCES user_type(ut_recid);

ALTER TABLE payment_info
ADD FOREIGN KEY (u_recid) REFERENCES users(u_recid),
ADD FOREIGN KEY (cc_recid) REFERENCES card_company(cc_recid);
