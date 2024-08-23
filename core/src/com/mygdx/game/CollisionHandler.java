package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

public class CollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() instanceof Projectile) {
            handleProjectileCollision((Projectile) fixtureA.getUserData(), fixtureB);
        } else if (fixtureB.getUserData() instanceof Projectile) {
            handleProjectileCollision((Projectile) fixtureB.getUserData(), fixtureA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // Not needed for this example
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Not needed for this example
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Not needed for this example
    }

    private void handleProjectileCollision(Projectile projectile, Fixture otherFixture) {
        if (otherFixture.getUserData() instanceof Block) {
            Block block = (Block) otherFixture.getUserData();
            if (projectile.getHealth() > 0) {
                Vector2 normal = otherFixture.getBody().getPosition().sub(projectile.body.getPosition()).nor();
                Vector2 velocity = projectile.body.getLinearVelocity();
                float dotProduct = velocity.dot(normal);
                Vector2 reflection = velocity.sub(normal.scl(2 * dotProduct));
                projectile.body.setLinearVelocity(reflection);
                projectile.decreaseHealth(); // Decrease health by 1 for each collision
            } else {
                projectile.body.getWorld().destroyBody(projectile.body);
            }
        }
    }
}
