package gmmre.phyco;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KonsultasiPasienActivity extends AppCompatActivity {


    private float treshold = (float) 0.75;
    private float min = 0;

    private FirebaseAuth mAuth;
    String uid;
    FirebaseUser user;

    private String kode;
    String solusi;

    private MyAdapter adapter;

    private ListView listView;
    private DatabaseReference mDatabaseMasalah, mDatabasePasien;
    private ArrayList<Base> mBaseData;
    Base base;

    String gender;

    private TextView mpasien;;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi_pasien);


    }

    @Override
    protected void onStart() {
        super.onStart();
        listView = (ListView) findViewById(R.id.lvMasalah);
        mBaseData = new ArrayList<Base>();



        mDatabaseMasalah = FirebaseDatabase.getInstance().getReference("Base");
        mDatabaseMasalah.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Base b = dataSnapshot1.getValue(Base.class);
                    mBaseData.add(b);
                }
                adapter = new MyAdapter(KonsultasiPasienActivity.this, mBaseData);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(KonsultasiPasienActivity.this, "Gagal mencari data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mencariSolusi(View view) {



        if (adapter.mBase.size() > 0) {
            final List<String> semuaKode = new ArrayList<>();
            final Map<String, Float> hasilKemiripan = new HashMap<>();

            for (Base b : adapter.mBase) {
                semuaKode.add(b.getKode());
            }

            DatabaseReference kasus = FirebaseDatabase.getInstance().getReference("Kasus");
            kasus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataKasus : dataSnapshot.getChildren()) {
                        float kemiripan = 0;
                        for (DataSnapshot gejalaKasus : dataKasus.child("Base").getChildren()) {
                            if (semuaKode.contains(gejalaKasus.getValue().toString())) {
                                kemiripan++;
                            }
                        }

                        float pembagi = Math.max((float) (dataKasus.child("Base").getChildrenCount()), semuaKode.size());

                        float hasil_bagi = kemiripan / pembagi;

                        hasilKemiripan.put(dataKasus.getKey(), hasil_bagi);
                    }

                    for (Map.Entry<String, Float> entry : hasilKemiripan.entrySet()) {
                        if (entry.getValue() > min) {
                            min = entry.getValue();
                            kode = entry.getKey();
                        }
                    }

                    if (kode != null) {
                        System.out.println(kode + " - " + min);
                        gejala(kode, min);
                    } else {
                        Toast.makeText(KonsultasiPasienActivity.this, "Gejala yang mirip tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(KonsultasiPasienActivity.this, "Gagal mencari data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(KonsultasiPasienActivity.this, "Anda belum mengisi gejala yang pasien anda rasakan", Toast.LENGTH_SHORT).show();
        }
    }

    private void gejala(final String kode, final float min) {
        final String kode_kasus = kode;
        final int nilai_kemiripan = (int) (min * 100);


        mDatabaseMasalah = FirebaseDatabase.getInstance().getReference().child("Base");

        DatabaseReference refMasalah = FirebaseDatabase.getInstance().getReference("Kasus").child(kode_kasus);
        refMasalah.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String diagnosaBase = dataSnapshot.child("solusi").getValue().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();

                Intent keyIntent = getIntent();
                final String namaPasien = keyIntent.getStringExtra("nmPasien");
                final String usiaPasien = keyIntent.getStringExtra("usPasien");


                mAuth = FirebaseAuth.getInstance();




                if (diagnosaBase.equals("Berikan kesempatan untuk dia dapat menyatakan atau meluapkan emosi dengan jalan melakukan perbuatan atau ucapan yang bersifat eksplosif, mengendalikan kekecewaan, mencari pembelaan. Diberikan kepercayaan dan yakin bahwa dia mempunyai kemampuan untuk melakukan sesuatu dan perlu diapresiasi, namun tetap harus bisa mengontrol ekspektasi. Selalu mendekatkan diri pada Tuhan, Menyibukkan diri dengan hobi disukai dan mulailah untuk memberikan kesempatan diri membuka hati dan pikiran bagi dunia luar.")
                        || diagnosaBase.equals("Dapat dilakukan Terapi Alam (Wilderness Therapy) maupun Terapi Keluarga (Family Therapy) dengan pendampingan oleh psikolog. dibarengi dengan meyakinkan diri sendiri, serta bersabar. Kita sendiri yang membuat diri kita menjadi perokok, dan kita sendiri pula yang bisa membuat diri sendiri hidup nyaman tanpa merokok. Sebaiknya berikan waktu istirahat sejenak pada tubuh dan pikiran, dan mulai bangun motivasi diri yang kuat dan cobalah lebih memahami konsep waktu dan milikilah mental bersegera dalam kebaikan dan ampunan-Nya.")
                        || diagnosaBase.equals("Identifikasi penyebab terlebih dahulu. Ditanyakan dengan baik - baik tanpa paksaan dan rasa menyalahkan. Melakukan pendekatan melalui keluarga terlebih dahulu serta diberikan dukungan untuk melakukan hal positif sehingga dapat meningkatkan kepercayaan diri. Adapun dalam pendekatan harus dilakukan secara sabar dan tidak tergesa gesa serta saling mendengarkan perasaan satu sama lain agar terjalinnya komunikasi yang baik antar keluarga. Apabila dalam pendekatan melalui keluarga sudah berhasil namun karakter remaja masih tidak banyak berubah maka remaja tersebut dibujuk untuk melakukan konsultasi secara face to face kepada psikolog untuk di treatment.")
                        || diagnosaBase.equals("Adapun dari permasalahan diatas, hal dasar yang harus kita ketahui adalah bagaimana kondisi keluarga  dan kondisi kehidupan di lingkungan rumah. Karena apabila keadaan keluarga atau rumah sudah termasuk tidak kondusif dan tidak nyaman dirasakan oleh sang anak atau remaja akan menumbuhkan beberapa sifat yang negatif, seperti  merasa kesepian, mudah frustasi dan selalu kesepian.  Perasan - perasaan tersebut jelas memberikan dampak yang negatif sehingga dapat melakukan hal yang tidak sepatutnya dilakukan. Perasaan seperti sangat tertutup menjadi pilihan remaja karena kurangnya kepercayaan dirinya serta kepercayaan terhadap orang lain. Sehingga, cobalah membangun kondisi rumah yang nyaman dan sebagai orang tua mencoba lebih interaktif untuk menanyakan perasaan sang anak. Apabila tidak berhasil oleh orang tua cobalah pendekatan melalui teman terdekat yang ia punya dan hindari perasaan kesepian karena pada hakikatnya kita punya sang Pencipta.")
                        || diagnosaBase.equals("Mengubah mindset terhadap hal - hal yang dapat merugikan diri sendiri dan lingkungan sekitar. Apabila remaja belum bisa berfikir ke arah merubah mindset itu secara individu, didukung dengan lingkungan terdekatnya yaitu keluarga. Adapun dalam pengobatan remaja atau pasien meliputi sisi biologis (psikofarmaka) dan sisi psikologis (psikoterapi).")
                        || diagnosaBase.equals("Melakukan pendekatan secara bertahap terhadap kejadian yang telah terjadi. Memberikan pengertian bahwa segala sesuatunya akan selesai. Adapun yang paling penting adalah menjalin komunikasi yang baik, utarakan segala apa yang dirasakan secara bertahap. Pikirkan hal yang positif dan tidak mendekati hal yang negatif. Cobalah bercerita kepada teman yang dipercaya sehingga perasaan “sendiri” bisa diminimalisir. Pendekatan dapat berupa kepercayaan, rasa aman dan kejujuran dari dalam hati. Pikirkan dampaknya akan terkena siapa saja apabila kejadian tersebut terjadi.")
                        || diagnosaBase.equals("Tetap menunjukkan rasa hormat kepada orang tua maupun seluruh anggota keluarga. mengatur mindset bahwa semuanya baik baik saja. Melakukan pendekatan terhadap anggota keluarga agar kepercayaan satu sama lain terjalin dengan baik. Komunikasikan segala sesuatunya baik dari segi keuangan atau masalah lainnya untuk mencapai solusi bersama. Meningkatkan keterampilan serta berusaha lebih keras untuk mendapatkan hasil yang baik dan positif.")
                        || diagnosaBase.equals("Meningkatkan keyakinan diri. Keyakinan seseorang bahwa ia mempunyai kemampuan untuk melakukan sesuatu dan mampu menyelesaikan tugas-tugas yang diberikan meskipun dengan keluarga  yang tidak lengkap dapat  memperoleh hasil yang maksimal. Hal ini juga lebih baik dengan memulai untuk tidak menuruti kemauannya secara bertahap, memberikan pengertian secara bertahap bahwa segala sesuatunya bisa didapatkan dengan usaha. Carilah Akar Permasalahan supaya tidak berlarut-larut dalam kesedihan dan berdiam diri. Jangan Pesimis, dan selalu ingat tujuan anda, temukan alasan terbaik untuk maju.")
                        || diagnosaBase.equals("Meningkatkan keyakinan diri bahwa seseorang mempunyai kemampuan untuk melakukan sesuatu serta coba lakukan pendekatan pada anak ataupun anak kepada orang tua, cobalah untuk respect satu dan yang lainnya. Ajak bicara, untuk menghilangkan frustasi yaitu nyatakan. bekali diri dengan pendidikan, pengetahuan tentang bahaya penyalahgunaan narkoba atau ikutilah rehabilitasi di pusat rehabilitasi untuk memudahkannya karena dipantau langsung oleh ahli. Untuk masalah kecanduan game, bisa dilakukan dengan terapi alam maupun terapi keluarga.")
                        || diagnosaBase.equals("Perlu adanya di apresiasi atau berusaha untuk mengapresiasi diri sendiri guna meningkatkan keyakinan diri bahwa dirinya mempunyai kemampuan untuk melakukan sesuatu, termasuk keyakinan untuk bisa berhenti merokok karena kita sendiri pula yang bisa membuat diri sendiri hidup nyaman tanpa merokok. Stop membanding-bandingkan dengan orang lain. Belajar untuk mengontrol diri agar jangan sampai terjerumus pada tindakan kriminal, segera mendiskusikannya dengan orang terkait, atau bercerita pada orang terdekat. Bagi orang tua untuk lebih meningkatkatkan perhatiannya terhadap anak.")
                        || diagnosaBase.equals("Memberikan pengertian kepada orang tua bahwa mampu untuk menghadapi dan menyelesaikan masalahnya sendiri. Membangun komunikasi yang baik, salah satunya dengan cara menyampaikan keluh dan semuanya kepada orang tua. karena nantinya yang menjalankan juga diri sendiri. Belajar memahami konsep waktu, dapat pula meminta bantuan psikolog untuk terapi alam maupun keluarga. Miliki juga mental bersegera dalam kebaikan dan ampunan-Nya.")
                        || diagnosaBase.equals("Berusaha untuk menyesuaikan atau bertindak sesuai sifat dia yang introvert. Jangan memaksakan kehendak. Bangun keyakinan pada dirinya bahwa dia mampu melakukan semuanya. Jangan ungkap rahasia apapun di hadapannya untuk tetap mendapat kepercayaan darinya. Sehingga dia mau secara leluasa menceritakan bahkan hal rahasia pada kita.  Jangan menghakimi atas cerita atau curhatannya dan ajak dia untuk menyibukkan diri mulai dari hobi.  Mulailah juga untuk mengajak dan memberikan kesempatan baginya membuka hati dan pikiran bagi dunia luar.")
                        || diagnosaBase.equals("Tetap tunjukkan rasa hormat kepada kedua orang tua walaupun dalam keadaan bertengkar, karena bagaimana pun mereka tetap orang tua kita. Tunjukan sikap netral supaya tidak menambah ketegangan di rumah. Sebagai seorang anak menanamkan mindset yang baik untuk berusaha tidak menyusahkan orang tua namun tidak meninggalkan tanggung jawab sebagai anak. Komunikasikan dengan baik kepada kedua orang tua. Tinggalkan sikap menyalahkan, cobalah untuk bercermin diri : Apakah ucapan atau tindakan saya salah? Bila memang salah, akuilah karena kebiasaan menyalahkan orang lain akan membuat kepercayaan kepada kita semakin pudar dan semakin luntur.")
                        || diagnosaBase.equals("Meningkatkan keyakinan diri. Keyakinan seseorang bahwa ia mempunyai kemampuan untuk melakukan sesuatu meski tanpa orang tua yang lengkap. Mendekatkan diri pada Tuhan, dengan banyak mengingat Tuhan, maka hati, pikiran, jiwa dan raga akan tenang dan damai. Menyibukkan diri dengan rangkaian hobi atau minat yang disukai. Mulailah untuk memberikan kesempatan diri membuka hati dan pikiran bagi dunia luar. Tetap berfikir positif, karena jumlah anak atau saudara merupakan takdir. Cobalah managing terhadap anggota keluarga dengan baik. Komunikasikan dengan baik kepada orang terdekat atau orang tua, jangan sampai pendidikan menjadi korban.")
                        || diagnosaBase.equals("Intinya, komunikasi merupakan hal yang penting. komunikasikan masalah atau hal apapun yang mengganjal. Gunakan kepala dingin, lebih bisa untuk mengendalikan emosi. Sikap saling pengertian antara anak dan orang tua, antar saudara juga sangat dibutuhkan dalam kasus perbedaan pendapat seperti ini. Orang tua bukannya dilarang untuk ikut andil dalam menentukan pilihan hidup sang anak. Tapi, dalam batas-batas tertentu yakni hanya sebagai saran untuk pertimbangan. Sehingga, sang anak bisa memutuskan pilihannya sendiri.")
                        || diagnosaBase.equals("Tanpa disadari apabila remaja atau anak sering dituruti kemauannya akan menjadi seseorang yang manja, cenderung egois dan marah apabila tidak dituruti kemauannya. Mulailah tidak menuruti kemauannya secara bertahap, memberikan pengertian secara bertahap bahwa segala sesuatunya bisa didapatkan dengan usaha. Sebagai anak pun harus mencoba untuk bisa menerima jika orang tua tidak bisa memenuhi permintaannya. Perbaiki kualitas komunikasi dalam keluarga. Berusaha untuk tidak terlalu mengekang dan cobalah untuk menyamakan persepsi. Berikan kepercayaan secara bertahap, disesuaikan dengan umur si anak. Hal ini akan berdampak pada perasaan anak menjadi nyaman bersama orang tua. Biasakan untuk bercermin terhadap diri sendiri.")
                        || diagnosaBase.equals("Komunikasi harus tetap dijaga dan saling pengertian antara anak dan orang tua sangat dibutuhkan dalam kasus perbedaan pendapat seperti ini. Orang tua juga berhak dalam memberi saran dan sebagai anak harus bisa menerima selama itu positif untuk dipertimbangkan. Mulailah meniatkan untuk menabung, pahami kondisi keuangan keluarga. Belajar untuk ikhlas dan menerima apapun kondisi dan keadaan diri sendiri. Psikoterapi bisa menjadi solusi baik jika memiliki rasa khawatir berlebihan yang kerap kali muncul.")
                        || diagnosaBase.equals("Coba lakukan pendekatan pada keluarga, saat santai ajak bicara. Jadikan obrolan ini sebagai perbincangan santai, untuk menciptakan kenyamanan di rumah bersama orang tua. Tetaplah melakukan hal berikut untuk mengatasi rasa percaya diri seperti, berfikir positif, menghargai diri sendiri, tetap melakukan introspeksi diri dalam rangka mengenali kekurangan dan kelebihan diri, mengenali diri anda sebenarnya, jangan selalu menyalahkan orang lain, apalagi terhadap permasalahan diri sendiri dan cobalah untuk  menghargai pencapaian diri anda. Nyatakan, kendalikan kekecewaan, cari pembelaan, seimbangkan batin, agar emosi dapat tersalurkan dengan baik.")
                        || diagnosaBase.equals("Jangan panik menghadapi keadaan dan cobalah menerima kenyataan. Dan berikan keyakinan pada diri bahwa mempunyai kemampuan untuk menghadapi semuanya meski dalam kehidupan yang kurang sempurna sekalipun. Tingkatkan motivasi diri. Coba buat rencana keuangan lain dan ubah masa-masa sulit menjadi sebuah peluang lain.")
                        || diagnosaBase.equals("Cobalah untuk membuka komunikasi dan diskusi yang baik dengan orang tua. ungkapkan semua yang terdapat dalam diri agar tidak menjadi beban. Berusahalah untuk menjelaskan dengan logis tujuan dari hal yang diinginkan. diskusikan untuk menyamakan persepsi dengan orang tua. komunikasi dan saling pengertian dengan orang tua sangat dibutuhkan dalam kasus perbedaan pendapat seperti ini. Carilah Akar Permasalahan yang Membuat Anda Pesimis, Ingat tujuan anda, temukan alasan terbaik untuk maju. apresiasi diri sendiri, Berhenti membanding-bandingkan. atau dapat melakukan psikoterapi dengan ahli untuk melakukan psikoterapi interpersonal")
                        || diagnosaBase.equals("Tetap memperlakukan orang tua hormat, menunjukan rasa respect, karena tidak menghormati hanya akan menambah ketegangan dalam keluarga. Tetap bersikap netral, dan mencoba untuk mengkomunikasikan dengan orang tua dan saudara. Coba untuk komunikasikan terkait apa yang dirasakan oleh diri sendiri baik kepada orang tua, saudara, maupun teman. Sebaiknya pilih waktu yang tepat untuk mengkomunikasikannya. Sebaiknya berikan waktu istirahat sejenak pada tubuh dan pikiran, dan mulai bangun motivasi diri yang kuat, menyibukan dengan kegiatan lain sehingga tidak terfokus pada masalah dan prestasi belajar/ konsentrasi belajar tidak terganggu dengan masalah dari luar.")
                        || diagnosaBase.equals("Mulailah untuk membuka komunikasi dan diskusi antar orang tua , orang tua kepada anak untuk menjelaskan tujuan dari aturan yang mereka buat. Usahakan untuk menghindari konflik terutama di depan anak meskipun terdapat perbedaan dalam hal pola asuh. orang tua juga perlu melihat diri sendiri terlebih dahulu sebelum melimpahkan semua kesalahan pada anak. Selalu komunikasikan dengan jelas disertai alasan untuk hal yang setuju maupun tidak disetujui. Berikan pula kepercayaan secara bertahap, disesuaikan dengan umur si anak. Ajak anak untuk berbicara secara terbuka sehingga anak tidak merasa diawasi dan dihakimi. Hal ini akan berdampak pada perasaan anak menjadi nyaman bersama orang tua dan senang bersama orang tua.")
                        || diagnosaBase.equals("Cobalah untuk membicarakan dengan cara yang baik dan mudah ditangkap. Berikan penjelasan/alasan logis untuk penolakan atas keinginan anak. Lakukan pula pendekatan pada anak dengan bicara. Jadikan obrolan sebagai perbincangan santai, jangan menyalahkan atau menyudutkan anak. Dengan begitu pengajaran dengan mendidik karakter yaitu menanamkan nilai-nilai kedisiplinan pada dirinya akan lebih mudah. Tetap tenang dan Jangan panik, cobalah menerima kenyataan, coba buat rencana keuangan lain, Coba ubah masa-masa sulit menjadi sebuah peluang lain, Catat keuangan Anda. Ajaklah untuk mulai menabung, berikan penjelasan tentang kondisi keuangan dan kapan waktu berbelanja yang tepat. ")
                        || diagnosaBase.equals("Berikan dukungan secara mental kepada orang tua. Motivasi seorang anak sangat membantu meringankan beban mental orang tua menghadapi masalahnya. Cobalah untuk respect, tidak menyudutkan orang tua, mengkomunikasikan dengan orang tua mencari akar permasalahannya dan coba untuk berusaha menahan keinginan-keinginan yang mengeluarkan uang banyak. Sebaiknya berikan pula waktu istirahat sejenak pada tubuh dan pikiran, dan mulai bangun motivasi diri yang kuat, ingat tujuan kedepan, dan temukan alasan terbaik untuk maju.")
                        || diagnosaBase.equals("Mulai untuk meyakinkan diri bahwa anda mempunyai kemampuan untuk melakukan sesuatu meski tanpa orang tua yang lengkap. Mendekatkan diri pada Tuhan, serahkan semua perkara dan kehidupan kepada Tuhan. Cobalah untuk membuat rencana keuangan lain, diskusikan dengan anggota keluarga lain atau sanak saudara. Ubah masa-masa sulit menjadi sebuah peluang lain. berikan waktu istirahat sejenak pada tubuh dan pikiran, dan mulai bangun motivasi diri yang kuat, menyibukan dengan kegiatan lain sehingga tidak terfokus pada masalah dan prestasi belajar/ konsentrasi belajar tidak terganggu dengan masalah dari luar.")) {
                    if (nilai_kemiripan > treshold * 100) {



                        DatabaseReference simpan_hasil = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Hasil").push();



                        simpan_hasil.child("solusi").setValue(diagnosaBase);

                        solusi = diagnosaBase;


                        SimpleDateFormat format_tanggal = new SimpleDateFormat("dd MMMM yyyy");
                        SimpleDateFormat format_waktu = new SimpleDateFormat("HH:MM");
                        Date date = new Date();

                        String tanggal = format_tanggal.format(date);
                        String waktu = format_waktu.format(date);

                        simpan_hasil.child("nama").setValue(namaPasien);
                        simpan_hasil.child("usia").setValue(usiaPasien);
                        simpan_hasil.child("tanggal").setValue(tanggal);
                        simpan_hasil.child("jam").setValue(waktu);

                        Intent mainIntent = new Intent(KonsultasiPasienActivity.this, SolusiActivity.class);
                        mainIntent.putExtra("solusiPasien", solusi);
                        startActivity(mainIntent);
                    } else {
                        Intent mainIntent = new Intent(KonsultasiPasienActivity.this, KonsultasiPasienActivity.class);
                        startActivity(mainIntent);
                        Toast.makeText(KonsultasiPasienActivity.this, "Solusi tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(KonsultasiPasienActivity.this, "Data tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}