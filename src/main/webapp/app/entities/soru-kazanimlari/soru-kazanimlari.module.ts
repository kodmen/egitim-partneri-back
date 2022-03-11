import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SoruKazanimlariComponent } from './list/soru-kazanimlari.component';
import { SoruKazanimlariDetailComponent } from './detail/soru-kazanimlari-detail.component';
import { SoruKazanimlariUpdateComponent } from './update/soru-kazanimlari-update.component';
import { SoruKazanimlariDeleteDialogComponent } from './delete/soru-kazanimlari-delete-dialog.component';
import { SoruKazanimlariRoutingModule } from './route/soru-kazanimlari-routing.module';

@NgModule({
  imports: [SharedModule, SoruKazanimlariRoutingModule],
  declarations: [
    SoruKazanimlariComponent,
    SoruKazanimlariDetailComponent,
    SoruKazanimlariUpdateComponent,
    SoruKazanimlariDeleteDialogComponent,
  ],
  entryComponents: [SoruKazanimlariDeleteDialogComponent],
})
export class SoruKazanimlariModule {}
