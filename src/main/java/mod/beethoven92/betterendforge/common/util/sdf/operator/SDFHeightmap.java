package mod.beethoven92.betterendforge.common.util.sdf.operator;

import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.math.MathHelper;

public class SDFHeightmap extends SDFDisplacement {
    private float intensity = 1F;
    private NativeImage map;
    private float offsetX;
    private float offsetZ;
    private float scale;
    private float cos = 1;
    private float sin = 0;

    public SDFHeightmap() {
        setFunction((pos) -> {
            if (map == null) {
                return 0F;
            }
            float px = MathHelper.clamp(pos.getX() * scale + offsetX, 0, map.getWidth() - 2);
            float pz = MathHelper.clamp(pos.getZ() * scale + offsetZ, 0, map.getHeight() - 2);
            float dx = (px * cos - pz * sin);
            float dz = (pz * cos + px * sin);
            int x1 = MathHelper.floor(dx);
            int z1 = MathHelper.floor(dz);
            int x2 = x1 + 1;
            int z2 = z1 + 1;
            dx = dx - x1;
            dz = dz - z1;
            float a = (map.getPixelRGBA(x1, z1) & 255) / 255F;
            float b = (map.getPixelRGBA(x2, z1) & 255) / 255F;
            float c = (map.getPixelRGBA(x1, z2) & 255) / 255F;
            float d = (map.getPixelRGBA(x2, z2) & 255) / 255F;
            a = MathHelper.lerp(dx, a, b);
            b = MathHelper.lerp(dx, c, d);
            return -MathHelper.lerp(dz, a, b) * intensity;
        });
    }

    public SDFHeightmap setMap(NativeImage map) {
        this.map = map;
        offsetX = map.getWidth() * 0.5F;
        offsetZ = map.getHeight() * 0.5F;
        scale = map.getWidth();
        return this;
    }

    public SDFHeightmap setAngle(float angle) {
        sin = MathHelper.sin(angle);
        cos = MathHelper.cos(angle);
        return this;
    }

    public SDFHeightmap setScale(float scale) {
        this.scale = map.getWidth() * scale;
        return this;
    }

    public SDFHeightmap setIntensity(float intensity) {
        this.intensity = intensity;
        return this;
    }
}
