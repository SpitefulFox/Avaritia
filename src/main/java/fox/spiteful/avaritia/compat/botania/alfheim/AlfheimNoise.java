package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;



public class AlfheimNoise {

	public final long seed;
	private final double[] randomSource;
	
	private static int XSEED = 1619;
	private static int ZSEED = 31337;
	private static int NSEED = 1013;
	private static int NSHIFT = 8;
	
	public AlfheimNoise(long seed) {
		this.seed = seed;
		this.randomSource = new double[256*2];
		Random randy = new Random(this.seed);
		for (int i=0; i<256; i++) {
			double r = randy.nextDouble();
			double a = randy.nextDouble()*2*Math.PI;
			
			this.randomSource[i*2] = Math.sin(a)*r;
			this.randomSource[i*2+1] = Math.cos(a)*r;
		}
	}
	
	private double getNoise(double x, double z, double period, long seed) {
		double frequency = period == 0 ? 1.0 : 1.0/period;
		return this.calcNoise(x * frequency, z * frequency, seed);
	}
	
	private double calcNoise(double x, double z, long seed) {
		int x0 = (x > 0 ? MathHelper.floor_double(x) : MathHelper.floor_double(x)-1);
		int x1 = x0 + 1;
		int z0 = (z > 0 ? MathHelper.floor_double(z) : MathHelper.floor_double(z)-1);
		int z1 = z0 + 1;
		
		double xs = deriv1(x-x0);
		double zs = deriv1(z-z0);
		
		double tl = this.gradientNoise(x,z, x0,z0, seed);
		double tr = this.gradientNoise(x,z, x1,z0, seed);
		double top = lerp(tl,tr, xs);
		
		double bl = this.gradientNoise(x,z, x0,z1, seed);
		double br = this.gradientNoise(x,z, x1,z1, seed);
		double bot = lerp(bl,br, xs);
		
		return lerp(top, bot, zs);
	}
	
	private double gradientNoise(double fx, double fz, int ix, int iz, long seed) {
		Vec3 grad = this.getGradient(ix, iz, seed);
		Vec3 point = Vec3.createVectorHelper(fx-ix, fz-iz, 0);
		
		return grad.dotProduct(point) * 2.12;
	}
	
	private Vec3 getGradient(int x, int z, long seed) {
		long vectorindex = (
			XSEED * x
			+ ZSEED * z
			+ NSEED * seed
		) & 0xffffffff;
		vectorindex ^= (vectorindex >> NSHIFT);
		vectorindex &= 0xff;
		int vi = (int)vectorindex;
		
		return Vec3.createVectorHelper(this.randomSource[vi*2], this.randomSource[vi*2+1], 0);
	}
	
	private Vec3 calcDerivative(double x, double z, long seed) {
		int ix = MathHelper.floor_double(x);
		int iz = MathHelper.floor_double(z);
		double fx = x-ix;
		double fz = z-iz;
		
		double wx = deriv1(fx);
		double wz = deriv1(fz);
		
		double dwx = deriv2(fx);
		double dwz = deriv2(fz);
		
		double dwpx = deriv3(fx);
		double dwpz = deriv3(fz);
		
		Vec3 vtl = getGradient(ix,   iz,   seed);
		Vec3 vtr = getGradient(ix+1, iz,   seed);
		Vec3 vbl = getGradient(ix,   iz+1, seed);
		Vec3 vbr = getGradient(ix+1, iz+1, seed);
		
		double tl = vtl.xCoord * (x - ix) + vtl.yCoord * (z - iz);
		double tr = vtr.xCoord * (x - (ix+1)) + vtr.yCoord * (z - iz);
		double bl = vbl.xCoord * (x - ix) + vbl.yCoord * (z - (iz+1));
		double br = vbr.xCoord * (x - (ix+1)) + vbr.yCoord * (z - (iz+1));
		
		double top = lerp(tl,tr,wx);
		double bot = lerp(bl,br,wx);
		double n = lerp(top,bot,wz);
		
		double dx = (vtl.xCoord + (vbl.xCoord-vtl.xCoord)*wz) + ((vtr.yCoord-vtl.yCoord)*fz - vtr.xCoord +
	               ((vtl.yCoord-vtr.yCoord-vbl.yCoord+vbr.yCoord)*fz + vtr.xCoord + vbl.yCoord - vbr.xCoord - vbr.yCoord)*wz)*
             dwx + ((vtr.xCoord-vtl.xCoord) + (vtl.xCoord-vtr.xCoord-vbl.xCoord+vbr.xCoord)*wz)*dwpx;
		
		double dz = (vtl.yCoord + (vtr.yCoord-vtl.yCoord)*wx) + ((vbl.xCoord-vtl.xCoord)*fx - vbl.yCoord + ((vtl.xCoord-
	               vtr.xCoord-vbl.xCoord+vbr.xCoord)*fx + vtr.xCoord + vbl.yCoord - vbr.xCoord - vbr.yCoord)*wx)*dwz +
	               ((vbl.yCoord-vtl.yCoord) + (vtl.yCoord-vtr.yCoord-vbl.yCoord+vbr.yCoord)*wx)*dwpz;
		
		return Vec3.createVectorHelper(n*2, dx*2, dz*2);
	}
	
	private double getNoiseOctaves(double x, double z, int octaves, double period, double lacunarity, double gain, long seed) {
		double sum = 0.0;
		double freq = period == 0 ? 1.0 : 1.0 / period;
		double amp = 1.0;
		
		for (int i=0; i<octaves; i++) {
			double n = this.getNoise(x, z, period*i, seed + i);
			sum += n*amp;
			freq *= lacunarity;
			amp *= gain;
		}
		
		return sum;
	}
	
	private double getJordanRaw(double x, double z, int octaves, double period, double lacunarity, double gain0, double gain, double warp0, double warp, double damp0, double damp, double dampscale, long seed) {
		double amp = gain0;
		double freq = (period == 0 ? 1.0 : 1.0 / period) * lacunarity;
		double damped_amp = amp * gain;
		
		Vec3 n = this.calcDerivative(x*freq, z*freq, seed);
		Vec3 n2 = Vec3.createVectorHelper(n.xCoord*n.xCoord, n.yCoord*n.xCoord, n.zCoord*n.xCoord);
		double sum = n2.xCoord;
		double dswx = n2.yCoord * warp0;
		double dswz = n2.zCoord * warp0;
		double dsdx = n2.yCoord * damp0;
		double dsdz = n2.zCoord * damp0;
		double dsddot;
		
		for (int i=1; i<octaves; i++) {
			n = this.calcDerivative(x*freq + dswx, z*freq + dswz, seed + i);
			n2 = Vec3.createVectorHelper(n.xCoord*n.xCoord, n.yCoord*n.xCoord, n.zCoord*n.xCoord);
			sum += damped_amp * ((n2.xCoord * 1.5) +0.1);
			dswx += n2.yCoord * warp;
			dswz += n2.zCoord * warp;
			dsdx += n2.yCoord * damp;
			dsdz += n2.zCoord * damp;
			freq *= lacunarity;
			amp *= gain;
			dsddot = dsdx*dsdx + dsdz*dsdz;
			damped_amp = amp * (1.0-dampscale/(1.0+dsddot));
		}
		return sum;
	}
	
	public double getJordan(double x, double z, int octaves, double period, double lacunarity, double gain0, double gain, double warp0, double warp, double damp0, double damp, double dampscale, int distortionoctaves, double distortionscale, double distortion, double distortiongain, long seed) {
		x += this.getNoiseOctaves(x, z, distortionoctaves, period * distortionscale, lacunarity, distortiongain, seed + 1) * distortion * period * distortionscale;
		z += this.getNoiseOctaves(x, z, distortionoctaves, period * distortionscale, lacunarity, distortiongain, seed + 2) * distortion * period * distortionscale;
		return this.getJordanRaw(x, z, octaves, period, lacunarity, gain0, gain, warp0, warp, damp0, damp, dampscale, seed);
	}
	
	public double getJordanDefault(double x, double z) {
		return this.getJordan(x, z, 4, 750.0, 4, 0.9, 0.7, 0.8, 0.6, 1.0, 0.8, 1.0, 1, 0.001, 0.35, 0.5, this.seed);
	}
	
	private double deriv1(double n) {
		return n * n * n * (n * (n * 6 - 15) + 10);
	}
	
	private double deriv2(double n) {
		return n * n * (n * (n * 30 - 60) + 30);
	}
	
	private double deriv3(double n) {
		return n * n * n * (n * (n * 36 - 75) + 40);
	}
	
	private double lerp(double a, double b, double n) {
		return a*(1.0-n) + b*n;
	}
	
	
}
